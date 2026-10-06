package pe.kallpa.whatsapp.api;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pe.kallpa.whatsapp.api.dto.ApiEnvelope;
import pe.kallpa.whatsapp.api.dto.BusinessInput;
import pe.kallpa.whatsapp.api.dto.BusinessResult;
import pe.kallpa.whatsapp.api.dto.BusinessResults;
import pe.kallpa.whatsapp.api.dto.NotificationRequest;
import pe.kallpa.whatsapp.service.MessagePreparationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("/api/v1/notificaciones/whatsapp")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    MessagePreparationService messagePreparationService;

    @POST
    public Response prepare(@Valid NotificationRequest request) {
        Set<String> seenRucs = new HashSet<>();
        for (BusinessInput business : request.negocios()) {
            if (!seenRucs.add(business.ruc())) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiEnvelope.error(
                                400,
                                "La solicitud contiene RUC duplicados.",
                                "DUPLICATE_RUC",
                                "RUC repetido: " + business.ruc()))
                        .build();
            }
        }

        List<BusinessResult> results = request.negocios().stream()
                .map(business -> messagePreparationService.prepare(business, request.mensaje()))
                .toList();
        BusinessResults data = new BusinessResults(results.size(), results);
        String summary = results.size() == 1
                ? "Se procesó 1 negocio; no se realizó ningún envío."
                : "Se procesaron " + results.size() + " negocios; no se realizó ningún envío.";
        ApiEnvelope<BusinessResults> response = ApiEnvelope.success(
                summary, data);
        return Response.ok(response).build();
    }
}

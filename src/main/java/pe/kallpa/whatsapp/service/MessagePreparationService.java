package pe.kallpa.whatsapp.service;

import jakarta.enterprise.context.ApplicationScoped;
import pe.kallpa.whatsapp.api.dto.BusinessInput;
import pe.kallpa.whatsapp.api.dto.BusinessResult;

@ApplicationScoped
public class MessagePreparationService {

    public BusinessResult prepare(BusinessInput business, String messageBody) {
        String razonSocial = business.razonSocial().trim();
        String punctuation = razonSocial.endsWith(".") || razonSocial.endsWith("!")
                || razonSocial.endsWith("?") ? " " : ". ";
        String greeting = "Hola, " + razonSocial + punctuation
                + "Te saludamos de Kallpa Facturación.";
        String fullMessage = greeting + "\n\n" + messageBody.trim();
        boolean authorized = Boolean.TRUE.equals(business.autorizado());

        return new BusinessResult(
                business.ruc(),
                business.razonSocial(),
                business.celular(),
                business.autorizado(),
                business.fechaCulminacion(),
                authorized ? "PREPARADO" : "OMITIDO",
                authorized ? fullMessage : null,
                authorized
                        ? "Mensaje preparado; no se envió. No hay un proveedor de mensajería configurado."
                        : "No se preparó el mensaje porque el negocio no está autorizado para recibirlo.");
    }
}

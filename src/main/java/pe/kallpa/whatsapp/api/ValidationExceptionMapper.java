package pe.kallpa.whatsapp.api;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pe.kallpa.whatsapp.api.dto.ApiEnvelope;

import java.util.List;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        List<String> violations = exception.getConstraintViolations().stream()
                .map(this::describe)
                .sorted()
                .toList();
        ApiEnvelope<Void> response = ApiEnvelope.error(
                400, "La solicitud contiene datos inválidos.", "VALIDATION_ERROR", violations);
        return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }

    private String describe(ConstraintViolation<?> violation) {
        return violation.getPropertyPath() + ": " + violation.getMessage();
    }
}

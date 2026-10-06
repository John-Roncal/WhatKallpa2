package pe.kallpa.whatsapp.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/** Request simple para preparar mensajes dirigidos a una lista de negocios. */
public record NotificationRequest(
        @NotBlank @Size(max = 4096) String mensaje,
        @NotEmpty @Size(max = 1000) List<@NotNull @Valid BusinessInput> negocios) {
}

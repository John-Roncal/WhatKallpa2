package pe.kallpa.whatsapp.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Datos de un negocio incluidos en la solicitud. */
public record BusinessInput(
        @NotBlank @Pattern(regexp = "\\d{11}") String ruc,
        @NotBlank @Size(max = 200) String razonSocial,
        @NotBlank @Pattern(regexp = "(?:\\+?51)?9\\d{8}") String celular,
        @NotNull Boolean autorizado,
        String fechaCulminacion) {
}

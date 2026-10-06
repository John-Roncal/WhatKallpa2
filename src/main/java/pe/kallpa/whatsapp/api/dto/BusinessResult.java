package pe.kallpa.whatsapp.api.dto;

/** Datos originales más el mensaje preparado y el estado para el reporte JSON. */
public record BusinessResult(
        String ruc,
        String razonSocial,
        String celular,
        Boolean autorizado,
        String fechaCulminacion,
        String estado,
        String mensaje,
        String detalle) {
}

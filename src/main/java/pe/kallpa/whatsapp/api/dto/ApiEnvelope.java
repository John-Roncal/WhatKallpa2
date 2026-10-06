package pe.kallpa.whatsapp.api.dto;

/** Sobre JSON estándar de salida. */
public record ApiEnvelope<T>(
        int statusCode,
        String message,
        T data,
        String errorCode,
        Object details) {

    public static <T> ApiEnvelope<T> success(String message, T data) {
        return new ApiEnvelope<>(200, message, data, null, null);
    }

    public static ApiEnvelope<Void> error(int statusCode, String message, String errorCode, Object details) {
        return new ApiEnvelope<>(statusCode, message, null, errorCode, details);
    }
}

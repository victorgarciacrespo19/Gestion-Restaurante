package es.upsa.ssbbdd2.trabajo2.domain.exceptions;

public class RestauranteException extends Exception {
    public RestauranteException() {
    }
    public RestauranteException(String message) {
        super(message);
    }
    public RestauranteException(String message, Throwable cause) {
        super(message, cause);
    }
    public RestauranteException(Throwable cause) {
        super(cause);
    }
    public RestauranteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

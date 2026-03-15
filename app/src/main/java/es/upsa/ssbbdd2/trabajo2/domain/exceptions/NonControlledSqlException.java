package es.upsa.ssbbdd2.trabajo2.domain.exceptions;

import java.sql.SQLException;

public class NonControlledSqlException extends RestauranteException{
    public NonControlledSqlException(Throwable cause) {
        super(cause);
    }

}

package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
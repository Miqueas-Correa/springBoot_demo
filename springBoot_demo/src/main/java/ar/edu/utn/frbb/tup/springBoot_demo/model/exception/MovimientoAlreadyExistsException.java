package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class MovimientoAlreadyExistsException extends RuntimeException {
    public MovimientoAlreadyExistsException(String message) {
        super(message);
    }
}

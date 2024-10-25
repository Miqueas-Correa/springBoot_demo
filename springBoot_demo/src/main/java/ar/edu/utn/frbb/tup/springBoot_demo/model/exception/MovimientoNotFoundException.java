package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class MovimientoNotFoundException extends RuntimeException {
    public MovimientoNotFoundException(String message) {
        super(message);
    }
}

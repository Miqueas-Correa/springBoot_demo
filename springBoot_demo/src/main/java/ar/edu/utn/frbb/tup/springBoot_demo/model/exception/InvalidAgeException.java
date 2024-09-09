package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class InvalidAgeException extends RuntimeException {
    public InvalidAgeException(String message) {
        super(message);
    }
}

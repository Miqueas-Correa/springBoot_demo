package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(String message) {
        super(message);
    }
}
package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class ClienteAlreadyExistsException extends Throwable {
    public ClienteAlreadyExistsException(String message) {
        super(message);
    }
}

package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class TipoCuentaAlreadyExistsException extends Exception {
    public TipoCuentaAlreadyExistsException(String message) {
        super(message);
    }
}

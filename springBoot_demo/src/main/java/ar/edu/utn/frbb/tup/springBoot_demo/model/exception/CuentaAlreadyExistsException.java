package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class CuentaAlreadyExistsException extends Throwable{
    public CuentaAlreadyExistsException(String message) {
        super(message);
    }
}

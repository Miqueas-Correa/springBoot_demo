package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class CuentaNotFoundException extends RuntimeException{
    public CuentaNotFoundException(String message) {
        super(message);
    }
}

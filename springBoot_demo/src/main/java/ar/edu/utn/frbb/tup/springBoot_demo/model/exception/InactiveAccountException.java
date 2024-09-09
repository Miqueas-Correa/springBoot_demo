package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class InactiveAccountException extends Exception {
    public InactiveAccountException(String message) {
        super(message);
    }
}

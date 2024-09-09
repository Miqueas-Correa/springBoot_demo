package ar.edu.utn.frbb.tup.springBoot_demo.model.exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

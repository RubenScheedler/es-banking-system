package dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

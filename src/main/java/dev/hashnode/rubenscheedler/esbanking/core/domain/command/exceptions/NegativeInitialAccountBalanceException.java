package dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions;

public class NegativeInitialAccountBalanceException extends RuntimeException {
    public NegativeInitialAccountBalanceException(String message) {
        super(message);
    }
}

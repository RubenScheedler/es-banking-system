package dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions;

public class NegativeCashDepositException extends RuntimeException {
    public NegativeCashDepositException(String message) {
        super(message);
    }
}

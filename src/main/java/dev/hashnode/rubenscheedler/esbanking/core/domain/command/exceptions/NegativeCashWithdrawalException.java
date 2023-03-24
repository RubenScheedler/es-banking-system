package dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions;

public class NegativeCashWithdrawalException extends RuntimeException {
    public NegativeCashWithdrawalException(String message) {
        super(message);
    }
}

package dev.hashnode.rubenscheedler.esbanking.core.service.exception;

public class AccountCouldNotBeViewedException extends RuntimeException {
    public AccountCouldNotBeViewedException(String message, Throwable cause) {
        super(message, cause);
    }
}

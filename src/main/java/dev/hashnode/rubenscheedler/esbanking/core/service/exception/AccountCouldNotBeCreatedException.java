package dev.hashnode.rubenscheedler.esbanking.core.service.exception;

/**
 * Thrown whenever an account could not be created.
 */
public class AccountCouldNotBeCreatedException extends Exception {
    public AccountCouldNotBeCreatedException(String message, Throwable cause) {
        super(message, cause);
    }
}

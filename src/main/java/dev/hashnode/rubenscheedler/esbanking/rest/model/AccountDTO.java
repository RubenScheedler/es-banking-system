package dev.hashnode.rubenscheedler.esbanking.rest.model;

import dev.hashnode.rubenscheedler.esbanking.core.domain.query.AccountView;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

/**
 * Represents a bank account
 */
@Value
@Builder
public class AccountDTO {
    /**
     * Unique identifier of the account
     */
    @NonNull
    UUID accountId;
    /**
     * Current balance of the account with two digits after the comma.
     * Example: 256,42
     */
    double balance;

    public static AccountDTO from(AccountView accountView) {
        return AccountDTO.builder()
                .accountId(accountView.getId())
                .balance((double) accountView.getBalance() / 100L)
                .build();
    }
}

package dev.hashnode.rubenscheedler.esbanking.rest.model;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class DepositDTO {
    /**
     * Account to deposit to
     */
    @NonNull
    UUID accountId;
    /**
     * Amount of money in cents to deposit
     */
    long amount;
}

package dev.hashnode.rubenscheedler.esbanking.core.domain.command.events;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class AccountCreatedEvent {
    UUID id;
    /**
     * Initial balance of the account in cents
     */
    long initialBalance;
}

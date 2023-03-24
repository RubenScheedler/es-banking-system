package dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands;

import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

/**
 * Command to create a new bank account.
 */
@Value
@Builder
public class CreateAccountCommand {
    @NonNull
    @TargetAggregateIdentifier
    UUID accountId;
    Money initialBalance;
}

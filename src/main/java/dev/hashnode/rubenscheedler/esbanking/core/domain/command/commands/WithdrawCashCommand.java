package dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands;

import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

/**
 * Command to reflect a cash withdrawal from an account.
 */
@Value
@Builder
public class WithdrawCashCommand {
    /**
     * Account that the money should be withdrawn from
     */
    @NonNull
    @TargetAggregateIdentifier
    UUID accountId;
    /**
     * Amount of cash that is being deposited
     */
    @NonNull
    Money amount;
}

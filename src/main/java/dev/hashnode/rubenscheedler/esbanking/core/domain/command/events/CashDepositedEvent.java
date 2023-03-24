package dev.hashnode.rubenscheedler.esbanking.core.domain.command.events;

import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class CashDepositedEvent {
    UUID accountId;
    Money amount;
}

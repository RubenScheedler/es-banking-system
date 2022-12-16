package dev.hashnode.rubenscheedler.esbanking.core.domain.command;

import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.CreateAccountCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.events.AccountCreatedEvent;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions.NegativeInitialAccountBalanceException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
@NoArgsConstructor
@Getter
public class Account {
    @AggregateIdentifier
    private UUID accountId;
    /**
     * Amount of money in the account in cents (to avoid rounding)
     */
    private long balance;

    @CommandHandler
    public Account(CreateAccountCommand command) {
        if (command.getInitialBalance() < 0L) {
            throw new NegativeInitialAccountBalanceException("An initial negative balance is not allowed.");
        }
        apply(AccountCreatedEvent.builder()
                .id(command.getAccountId())
                .initialBalance(command.getInitialBalance())
                .build()
        );
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        accountId = event.getId();
        balance = event.getInitialBalance();
    }
}

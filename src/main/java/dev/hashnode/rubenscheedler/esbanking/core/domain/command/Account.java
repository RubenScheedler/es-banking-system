package dev.hashnode.rubenscheedler.esbanking.core.domain.command;

import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.CreateAccountCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.DepositCashCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.events.AccountCreatedEvent;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.events.CashDepositedEvent;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions.NegativeCashDepositException;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions.NegativeInitialAccountBalanceException;
import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
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
    private Money balance;

    @CommandHandler
    public Account(CreateAccountCommand command) {
        if (command.getInitialBalance().getAmount() < 0L) {
            throw new NegativeInitialAccountBalanceException("An initial negative balance is not allowed.");
        }
        apply(AccountCreatedEvent.builder()
                .id(command.getAccountId())
                .initialBalance(command.getInitialBalance())
                .build()
        );
    }

    @CommandHandler
    public void depositCash(DepositCashCommand command) {
        if (command.getAmount().getAmount() < 0L) {
            throw new NegativeCashDepositException(String.format("A deposit of %d was attempted for account %s", command.getAmount().getAmount(), command.getAccountId()));
        }
        apply(CashDepositedEvent.builder()
                .accountId(command.getAccountId())
                .amount(command.getAmount())
                .build());
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        accountId = event.getId();
        balance = event.getInitialBalance();
    }

    @EventSourcingHandler
    public void on(CashDepositedEvent event) {
        balance = Money.builder().amount(balance.getAmount() + event.getAmount().getAmount()).build();
    }
}

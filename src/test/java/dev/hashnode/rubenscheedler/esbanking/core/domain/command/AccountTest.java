package dev.hashnode.rubenscheedler.esbanking.core.domain.command;

import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.CreateAccountCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.DepositCashCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.WithdrawCashCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.events.AccountCreatedEvent;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.events.CashDepositedEvent;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.events.CashWithdrawnEvent;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions.InsufficientFundsException;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions.NegativeCashDepositException;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions.NegativeCashWithdrawalException;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions.NegativeInitialAccountBalanceException;
import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AccountTest {
    private final FixtureConfiguration<Account> testFixture = new AggregateTestFixture<>(Account.class);

    private final UUID accountId = UUID.randomUUID();

    @Test
    void handleCreateAccountCommand_publishesAccountCreatedEvent() {
        testFixture
                .givenNoPriorActivity()
                .when(CreateAccountCommand.builder().accountId(accountId).initialBalance(Money.builder().amount(400L).build()).build())
                .expectEvents(defaultAccountCreatedEvent(400L)
                );
    }

    @Test
    void handleCreateAccountCommand_negativeInitialBalance_raisesException() {
        testFixture
                .givenNoPriorActivity()
                .when(CreateAccountCommand.builder().accountId(accountId).initialBalance(Money.builder().amount(-250L).build()).build())
                .expectException(NegativeInitialAccountBalanceException.class)
                .expectExceptionMessage("An initial negative balance is not allowed.");
    }

    @Test
    void handleDepositCashCommand_publishesCashDepositedEvent() {
        testFixture
                .givenCommands(CreateAccountCommand.builder().accountId(accountId).initialBalance(Money.builder().amount(400L).build()).build())
                .when(DepositCashCommand.builder().accountId(accountId).amount(Money.builder().amount(400L).build()).build())
                .expectEvents(CashDepositedEvent.builder()
                        .accountId(accountId)
                        .amount(Money.builder().amount(400L).build())
                        .build()
                );
    }

    @Test
    void handleDepositCashCommand_negativeAmount_raisesException() {
        testFixture
                .givenCommands(CreateAccountCommand.builder().accountId(accountId).initialBalance(Money.builder().amount(400L).build()).build())
                .when(DepositCashCommand.builder().accountId(accountId).amount(Money.builder().amount(-250L).build()).build())
                .expectException(NegativeCashDepositException.class)
                .expectExceptionMessage("A deposit of -250 was attempted for account " + accountId);
    }

    @Test
    void handleWithdrawCashCommand_publishesCashWithdrawnEvent() {
        testFixture
                .givenCommands(CreateAccountCommand.builder().accountId(accountId).initialBalance(Money.builder().amount(400L).build()).build())
                .when(WithdrawCashCommand.builder().accountId(accountId).amount(Money.builder().amount(400L).build()).build())
                .expectEvents(CashWithdrawnEvent.builder()
                        .accountId(accountId)
                        .amount(Money.builder().amount(400L).build())
                        .build()
                );
    }

    @Test
    void handleWithdrawCashCommand_negativeAmount_raisesException() {
        testFixture
                .givenCommands(CreateAccountCommand.builder().accountId(accountId).initialBalance(Money.builder().amount(400L).build()).build())
                .when(WithdrawCashCommand.builder().accountId(accountId).amount(Money.builder().amount(-250L).build()).build())
                .expectException(NegativeCashWithdrawalException.class)
                .expectExceptionMessage("A withdrawal of -250 was attempted for account " + accountId);
    }

    @Test
    void handleWithdrawCashCommand_amountLargerThanBalance_raisesException() {
        testFixture
                .givenCommands(CreateAccountCommand.builder().accountId(accountId).initialBalance(Money.builder().amount(400L).build()).build())
                .when(WithdrawCashCommand.builder().accountId(accountId).amount(Money.builder().amount(500L).build()).build())
                .expectException(InsufficientFundsException.class)
                .expectExceptionMessage("Account " + accountId + " has insufficient funds for a withdrawal of 500");
    }

    @Test
    void onAccountCreatedEvent_idIsSet() {
        // given
        Account state = new Account();
        // when
        state.on(AccountCreatedEvent.builder().id(accountId).build());
        // then
        assertThat(state.getAccountId()).isEqualTo(accountId);
    }

    @Test
    void onAccountCreatedEvent_balanceIsSet() {
        // given
        Account state = new Account();
        // when
        state.on(defaultAccountCreatedEvent(500L));
        // then
        assertThat(state.getBalance()).isEqualTo(Money.builder().amount(500L).build());
    }

    @Test
    void onCashDepositedEvent_balanceIncreased() {
        // given
        Account account = new Account();
        account.on(defaultAccountCreatedEvent(500L));
        // when
        account.on(CashDepositedEvent.builder().accountId(accountId).amount(Money.builder().amount(1000L).build()).build());
        // then
        assertThat(account.getBalance().getAmount()).isEqualTo(1500L);
    }

    @Test
    void onCashWithdrawnEvent_balanceDecreased() {
        // given
        Account account = new Account();
        account.on(defaultAccountCreatedEvent(1500L));
        // when
        account.on(CashWithdrawnEvent.builder().accountId(accountId).amount(Money.builder().amount(1000L).build()).build());
        // then
        assertThat(account.getBalance().getAmount()).isEqualTo(500L);
    }

    private AccountCreatedEvent defaultAccountCreatedEvent(long amount) {
        return AccountCreatedEvent.builder().id(accountId).initialBalance(Money.builder().amount(amount).build()).build();
    }
}

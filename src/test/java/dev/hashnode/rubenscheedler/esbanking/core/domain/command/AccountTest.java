package dev.hashnode.rubenscheedler.esbanking.core.domain.command;

import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.CreateAccountCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.events.AccountCreatedEvent;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.exceptions.NegativeInitialAccountBalanceException;
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
                .when(CreateAccountCommand.builder().accountId(accountId).initialBalance(400L).build())
                .expectEvents(AccountCreatedEvent.builder()
                        .id(accountId)
                        .initialBalance(400L)
                        .build()
                );
    }

    @Test
    void handleCreateAccountCommand_negativeInitialBalance_raisesException() {
        testFixture
                .givenNoPriorActivity()
                .when(CreateAccountCommand.builder().accountId(accountId).initialBalance(-250L).build())
                .expectException(NegativeInitialAccountBalanceException.class)
                .expectExceptionMessage("An initial negative balance is not allowed.");
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
        state.on(AccountCreatedEvent.builder().id(accountId).initialBalance(500L).build());
        // then
        assertThat(state.getBalance()).isEqualTo(500L);
    }
}

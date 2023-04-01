package dev.hashnode.rubenscheedler.esbanking.core.service;

import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.CreateAccountCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.DepositCashCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.WithdrawCashCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
import dev.hashnode.rubenscheedler.esbanking.core.domain.query.AccountView;
import dev.hashnode.rubenscheedler.esbanking.core.service.exception.AccountCouldNotBeCreatedException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    @Mock
    CommandGateway commandGateway;
    @Mock
    QueryGateway queryGateway;
    @InjectMocks
    AccountService accountService; // SUT

    @Test
    void createAccount_publishesCreateAccountCommand() throws AccountCouldNotBeCreatedException {
        // given
        AccountView expected = mock(AccountView.class);
        CompletableFuture<AccountView> resultFuture = CompletableFuture.completedFuture(expected);
        when(queryGateway.query(any(), eq(AccountView.class))).thenReturn(resultFuture);

        // when
        accountService.createAccount();
        // then
        verify(commandGateway).sendAndWait(argThat(c -> c instanceof CreateAccountCommand));
    }

    @Test
    void createAccount_publishesCommandWithEmptyBalance() throws AccountCouldNotBeCreatedException {
        // given
        AccountView expected = mock(AccountView.class);
        CompletableFuture<AccountView> resultFuture = CompletableFuture.completedFuture(expected);
        when(queryGateway.query(any(), eq(AccountView.class))).thenReturn(resultFuture);

        // when
        accountService.createAccount();

        // then
        verify(commandGateway).sendAndWait(argThat(c -> ((CreateAccountCommand)c).getInitialBalance().getAmount() == 0L));
    }

    @Test
    void createAccount_returnsQueriedCreatedAccount() throws AccountCouldNotBeCreatedException {
        // given
        AccountView expected = mock(AccountView.class);
        CompletableFuture<AccountView> resultFuture = CompletableFuture.completedFuture(expected);
        when(queryGateway.query(any(), eq(AccountView.class))).thenReturn(resultFuture);

        // when
        AccountView actual = accountService.createAccount();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void createAccount_interruptedExceptionOccurs_throwsException() {
        // given
        when(queryGateway.query(any(), eq(AccountView.class)))
                .thenReturn(CompletableFuture.failedFuture(new InterruptedException()));

        // when
        assertThatThrownBy(() -> accountService.createAccount())
        // then
                .isInstanceOf(AccountCouldNotBeCreatedException.class);
    }


    @Test
    void createAccount_executedExceptionOccurs_throwsException() {
        // given
        when(queryGateway.query(any(), eq(AccountView.class)))
                .thenReturn(CompletableFuture.failedFuture(new ExecutionException("Something went wrong", null)));
        // when
        assertThatThrownBy(() -> accountService.createAccount())
                // then
                .isInstanceOf(AccountCouldNotBeCreatedException.class);
    }

    @Test
    void depositCash_publishesDepositCashCommand() {
        // given
        UUID accountId = UUID.randomUUID();
        Money money = Money.builder().amount(500L).build();

        // when
        accountService.depositCash(accountId, money);

        // then
        verify(commandGateway).sendAndWait(argThat(c -> c instanceof DepositCashCommand));
    }

    @Test
    void depositCash_publishesCommand_withRightAccountId() {
        // given
        UUID expected = UUID.randomUUID();
        Money money = Money.builder().amount(500L).build();

        // when
        accountService.depositCash(expected, money);

        // then
        verify(commandGateway).sendAndWait(argThat(c -> ((DepositCashCommand)c).getAccountId().equals(expected)));
    }

    @Test
    void depositCash_publishesCommand_withRightAmount() {
        // given
        UUID accountId = UUID.randomUUID();
        Money expected = Money.builder().amount(500L).build();

        // when
        accountService.depositCash(accountId, expected);

        // then
        verify(commandGateway).sendAndWait(argThat(c -> ((DepositCashCommand)c).getAmount().equals(expected)));
    }

    @Test
    void withdrawCash_publishesWithdrawCashCommand() {
        // given
        UUID accountId = UUID.randomUUID();
        Money money = Money.builder().amount(500L).build();

        // when
        accountService.withdrawCash(accountId, money);

        // then
        verify(commandGateway).sendAndWait(argThat(c -> c instanceof WithdrawCashCommand));
    }

    @Test
    void withdrawCash_publishesCommand_withRightAccountId() {
        // given
        UUID expected = UUID.randomUUID();
        Money money = Money.builder().amount(500L).build();

        // when
        accountService.withdrawCash(expected, money);

        // then
        verify(commandGateway).sendAndWait(argThat(c -> ((WithdrawCashCommand)c).getAccountId().equals(expected)));
    }

    @Test
    void withdrawCash_publishesCommand_withRightAmount() {
        // given
        UUID accountId = UUID.randomUUID();
        Money expected = Money.builder().amount(500L).build();

        // when
        accountService.withdrawCash(accountId, expected);

        // then
        verify(commandGateway).sendAndWait(argThat(c -> ((WithdrawCashCommand)c).getAmount().equals(expected)));
    }
}

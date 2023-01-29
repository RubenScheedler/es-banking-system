package dev.hashnode.rubenscheedler.esbanking.core.service;

import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.CreateAccountCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.query.AccountView;
import dev.hashnode.rubenscheedler.esbanking.core.service.exception.AccountCouldNotBeCreatedException;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        verify(commandGateway).sendAndWait(argThat(c -> ((CreateAccountCommand)c).getInitialBalance() == 0L));
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
}

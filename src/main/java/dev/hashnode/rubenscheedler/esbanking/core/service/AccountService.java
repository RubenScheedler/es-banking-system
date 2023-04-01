package dev.hashnode.rubenscheedler.esbanking.core.service;

import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.CreateAccountCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.DepositCashCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.WithdrawCashCommand;
import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
import dev.hashnode.rubenscheedler.esbanking.core.domain.query.AccountView;
import dev.hashnode.rubenscheedler.esbanking.core.domain.query.queries.ViewAccountQuery;
import dev.hashnode.rubenscheedler.esbanking.core.service.exception.AccountCouldNotBeCreatedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.queryhandling.QueryGateway;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    public AccountView createAccount() throws AccountCouldNotBeCreatedException {
        UUID accountId = UUID.randomUUID();
        long initialBalance = 0L;
        CreateAccountCommand command = CreateAccountCommand.builder()
                .initialBalance(Money.builder().amount(initialBalance).build())
                .accountId(accountId)
                .build();
        log.debug(String.format("Account created with id %s and initialBalance %d", accountId, initialBalance));

        commandGateway.sendAndWait(command);

        try {
            return queryGateway.query(ViewAccountQuery.builder().accountId(accountId).build(), AccountView.class).get();
        } catch (InterruptedException | ExecutionException e) {
            log.debug(String.format("While querying account %s an exception occurred", accountId), e);
            throw new AccountCouldNotBeCreatedException("Account could not be created", e);
        }
    }

    /**
     * Performs a cash deposit for an account.
     * @param accountId Account to deposit on
     * @param money Money to deposit on the account
     */
    public void depositCash(UUID accountId, Money money) {
        commandGateway.sendAndWait(DepositCashCommand.builder()
                        .accountId(accountId)
                        .amount(money)
                .build());
    }

    /**
     * Performs a cash withdrawal for an account.
     * @param accountId Account to withdraw from
     * @param money Money to withdraw from the account
     */
    public void withdrawCash(UUID accountId, Money money) {
        commandGateway.sendAndWait(WithdrawCashCommand.builder()
                .accountId(accountId)
                .amount(money)
                .build());
    }
}

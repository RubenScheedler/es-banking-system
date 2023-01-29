package dev.hashnode.rubenscheedler.esbanking.core.service;

import dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands.CreateAccountCommand;
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
                .initialBalance(initialBalance)
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
}

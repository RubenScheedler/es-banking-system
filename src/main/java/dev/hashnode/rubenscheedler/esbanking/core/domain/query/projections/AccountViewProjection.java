package dev.hashnode.rubenscheedler.esbanking.core.domain.query.projections;

import dev.hashnode.rubenscheedler.esbanking.core.domain.command.events.AccountCreatedEvent;
import dev.hashnode.rubenscheedler.esbanking.core.domain.query.AccountView;
import dev.hashnode.rubenscheedler.esbanking.core.domain.query.queries.ViewAccountQuery;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AccountViewProjection {
    /**
     * I would prefer to extract state away from this singleton bean, but
     * for now I keep it here in line with the Axons recommendations.
     */
    private final ConcurrentHashMap<UUID, AccountView> accountViews;

    public AccountViewProjection() {
        accountViews = new ConcurrentHashMap<>();
    }

    @EventHandler
    public void on(AccountCreatedEvent e) {
        accountViews.put(e.getId(), AccountView.builder()
                        .id(e.getId())
                        .balance(e.getInitialBalance().getAmount())
                .build());
    }

    /**
     * Retrieves the current state of an account.
     * @param query Query with the ID of the account to retrieve.
     */
    @QueryHandler
    public Optional<AccountView> handle(ViewAccountQuery query) {
        return Optional.ofNullable(accountViews.get(query.getAccountId()));
    }
}

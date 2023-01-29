package dev.hashnode.rubenscheedler.esbanking.core.domain.query.projections;

import dev.hashnode.rubenscheedler.esbanking.core.domain.command.events.AccountCreatedEvent;
import dev.hashnode.rubenscheedler.esbanking.core.domain.query.AccountView;
import dev.hashnode.rubenscheedler.esbanking.core.domain.query.queries.ViewAccountQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AccountViewProjectionTest {
    @InjectMocks
    AccountViewProjection accountViewProjection;
    private final UUID accountId = UUID.fromString("7089a7bb-5640-495e-a813-8ef60e2f2c96");

    @Test
    void handleViewAccountQuery_accountExists_givesAccountView() {
        // given
        AccountCreatedEvent accountCreatedEvent = AccountCreatedEvent.builder()
                .id(accountId)
                .initialBalance(50L)
                .build();
        accountViewProjection.on(accountCreatedEvent);

        // when
        Optional<AccountView> actual = accountViewProjection.handle(ViewAccountQuery.builder()
                .accountId(accountId)
                .build());

        // then
        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getId()).isEqualTo(accountCreatedEvent.getId());
        assertThat(actual.get().getBalance()).isEqualTo(accountCreatedEvent.getInitialBalance());
    }

    @Test
    void handleViewAccountQuery_accountDoesNotExist_givesEmptyOptional() {
        // given
        // no create event

        // when
        Optional<AccountView> actual = accountViewProjection.handle(ViewAccountQuery.builder()
                .accountId(accountId)
                .build());

        // then
        assertThat(actual.isEmpty()).isTrue();
    }
}

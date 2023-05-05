package dev.hashnode.rubenscheedler.esbanking.rest.model;

import dev.hashnode.rubenscheedler.esbanking.core.domain.query.AccountView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class AccountDTOTest {
    @Test
    void from_givenValidAccount_returnsValidAccountDTO() {
        // given
        AccountView accountView = AccountView.builder()
                .id(UUID.randomUUID())
                .balance(100L)
                .build();

        // when
        AccountDTO actual = AccountDTO.from(accountView);

        // then
        assertThat(actual, notNullValue());
        assertThat(actual.getAccountId(), is(accountView.getId()));
        assertThat(actual.getBalance(), is((double)accountView.getBalance()/100L));
    }
}

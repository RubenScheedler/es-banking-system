package dev.hashnode.rubenscheedler.esbanking.core.domain.command.events;

import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
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
class CashDepositedEventTest {
    @Test
    void createCashDepositedEvent_withoutAccountId_throwsException() {
        assertThatThrownBy(() -> CashDepositedEvent.builder()
                // no account
                .amount(Money.builder().amount(500L).build())
                .build()
        )
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void createCashDepositedEvent_withoutAmount_throwsException() {
        assertThatThrownBy(() -> CashDepositedEvent.builder()
                .accountId(UUID.randomUUID())
                // no amount
                .build()
        )
                .isInstanceOf(NullPointerException.class);
    }
}

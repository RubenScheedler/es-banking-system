package dev.hashnode.rubenscheedler.esbanking.core.domain.command.commands;

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
class DepositCashCommandTest {
    @Test
    void depositCashCommand_cantBeCreatedWithoutAccount() {
        assertThatThrownBy(() -> DepositCashCommand.builder()
                .amount(Money.builder().amount(5L).build())
                // no account here
                .build()
        )
        .isInstanceOf(NullPointerException.class);
    }

    @Test
    void depositCashCommand_cantBeCreatedWithoutAmount() {
        assertThatThrownBy(() -> DepositCashCommand.builder()
                .accountId(UUID.randomUUID())
                // no amount here
                .build()
        )
        .isInstanceOf(NullPointerException.class);
    }
}

package dev.hashnode.rubenscheedler.esbanking.rest;

import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
import dev.hashnode.rubenscheedler.esbanking.core.service.AccountService;
import dev.hashnode.rubenscheedler.esbanking.rest.model.DepositDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class DepositControllerTest {
    @Mock
    AccountService accountService;
    @InjectMocks
    DepositController depositController;

    @Test
    void deposit_should_call_accountService_deposit() {
        // given
        DepositDTO depositDto = DepositDTO.builder()
                .accountId(UUID.randomUUID())
                .amount(100L)
                .build();

        // when
        depositController.deposit(depositDto);

        // then
        verify(accountService, times(1)).depositCash(depositDto.getAccountId(), Money.builder()
                .amount(depositDto.getAmount())
                .build());
    }
}

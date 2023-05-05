package dev.hashnode.rubenscheedler.esbanking.rest;

import dev.hashnode.rubenscheedler.esbanking.core.domain.query.AccountView;
import dev.hashnode.rubenscheedler.esbanking.core.service.AccountService;
import dev.hashnode.rubenscheedler.esbanking.core.service.exception.AccountCouldNotBeCreatedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Mock
    AccountService accountService;
    @InjectMocks
    AccountController accountController;

    @Test
    void createAccount_callsAccountServiceCreateAccount() throws AccountCouldNotBeCreatedException {
        // given
        when(accountService.createAccount()).thenReturn(AccountView.builder()
                        .id(UUID.randomUUID())
                        .build());
        // when
        accountController.createAccount();
        // then
        verify(accountService).createAccount();
    }
}

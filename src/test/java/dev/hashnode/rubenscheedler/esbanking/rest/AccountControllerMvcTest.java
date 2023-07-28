package dev.hashnode.rubenscheedler.esbanking.rest;

import dev.hashnode.rubenscheedler.esbanking.core.domain.query.AccountView;
import dev.hashnode.rubenscheedler.esbanking.core.service.AccountService;
import dev.hashnode.rubenscheedler.esbanking.core.service.exception.AccountCouldNotBeCreatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AccountController.class)
class AccountControllerMvcTest {
    private static final UUID ACCOUNT_ID = UUID.randomUUID();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;

    @BeforeEach
    void setUp() throws AccountCouldNotBeCreatedException {
        AccountView accountView = AccountView.builder()
                .id(ACCOUNT_ID)
                .balance(12040L)
                .build();
        when(accountService.createAccount()).thenReturn(accountView);
        when(accountService.getAccount(any())).thenReturn(Optional.of(accountView));
    }

    @Test
    void createAccount_shouldReturnCreated() throws Exception {
        mockMvc.perform(post("/api/accounts"))
                .andExpect(status().isCreated());
    }

    @Test
    void createAccount_shouldReturnCorrectLocation() throws Exception {
        mockMvc.perform(post("/api/accounts"))
                .andExpect(header().string("Location","/api/accounts/"+ACCOUNT_ID));
    }

    @Test
    void getAccount_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/accounts/"+ACCOUNT_ID))
                .andExpect(status().isOk());
    }

    @Test
    void getAccount_accountDoesNotExist_shouldReturnNotFound() throws Exception {
        when(accountService.getAccount(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/"+ACCOUNT_ID))
                .andExpect(status().isNotFound());
    }
}

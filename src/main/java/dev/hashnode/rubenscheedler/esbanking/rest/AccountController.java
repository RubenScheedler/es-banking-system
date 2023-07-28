package dev.hashnode.rubenscheedler.esbanking.rest;

import dev.hashnode.rubenscheedler.esbanking.core.domain.query.AccountView;
import dev.hashnode.rubenscheedler.esbanking.core.service.AccountService;
import dev.hashnode.rubenscheedler.esbanking.core.service.exception.AccountCouldNotBeCreatedException;
import dev.hashnode.rubenscheedler.esbanking.rest.model.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /**
     * Creates a new account
     */
    @PostMapping("/api/accounts")
    ResponseEntity<AccountDTO> createAccount() throws AccountCouldNotBeCreatedException {
        AccountView account = accountService.createAccount();
        return ResponseEntity.created(URI.create("/api/accounts/" + account.getId())).body(AccountDTO.from(account));
    }

    @GetMapping("/api/accounts/{accountId}")
    ResponseEntity<AccountDTO> getAccount(@PathVariable String accountId) {
        Optional<AccountView> accountOptional = accountService.getAccount(UUID.fromString(accountId));
        return accountOptional
                .map(accountView -> ResponseEntity.ok(AccountDTO.from(accountView)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

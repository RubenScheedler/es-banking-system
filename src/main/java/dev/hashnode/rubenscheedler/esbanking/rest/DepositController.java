package dev.hashnode.rubenscheedler.esbanking.rest;

import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
import dev.hashnode.rubenscheedler.esbanking.core.service.AccountService;
import dev.hashnode.rubenscheedler.esbanking.rest.model.DepositDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DepositController {
    private final AccountService accountService;
    /**
     * Deposit money into an account
     * @return The state of the account after the deposit
     */
    @PostMapping("/api/deposit")
    ResponseEntity<Void> deposit(@RequestBody DepositDTO depositDTO) {
        accountService.depositCash(
                depositDTO.getAccountId(),
                Money.builder().amount(depositDTO.getAmount()).build()
        );
        return ResponseEntity.ok().build();
    }
}

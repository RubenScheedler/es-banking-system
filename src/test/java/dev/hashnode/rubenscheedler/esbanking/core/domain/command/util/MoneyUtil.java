package dev.hashnode.rubenscheedler.esbanking.core.domain.command.util;

import dev.hashnode.rubenscheedler.esbanking.core.domain.model.value.Money;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MoneyUtil {
    /**
     * Constructs a Money object out of an amount.
     */
    public Money asMoney(long amount) {
        return Money.builder().amount(amount).build();
    }
}

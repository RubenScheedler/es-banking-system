package dev.hashnode.rubenscheedler.esbanking.core.domain.model.value;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Money {
    /**
     * Amount of money in cents
     */
    long amount;
}

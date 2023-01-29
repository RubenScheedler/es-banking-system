package dev.hashnode.rubenscheedler.esbanking.core.domain.query;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class AccountView {
    @NonNull
    UUID id;
    long balance;
}

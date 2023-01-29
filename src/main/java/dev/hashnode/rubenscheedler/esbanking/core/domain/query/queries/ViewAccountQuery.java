package dev.hashnode.rubenscheedler.esbanking.core.domain.query.queries;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ViewAccountQuery {
    @NonNull
    UUID accountId;
}

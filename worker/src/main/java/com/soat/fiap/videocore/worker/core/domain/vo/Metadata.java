package com.soat.fiap.videocore.worker.core.domain.vo;

import java.util.Objects;
import java.util.UUID;

/**
 * Objeto de valor que representa metadados de rastreabilidade do processamento.
 */
public record Metadata(UUID userId, UUID requestId) {

    public Metadata {
        validate(userId, requestId);
    }

    private static void validate(UUID userId, UUID requestId) {
        Objects.requireNonNull(userId, "userId não pode ser nulo");
        Objects.requireNonNull(requestId, "requestId não pode ser nulo");
    }
}
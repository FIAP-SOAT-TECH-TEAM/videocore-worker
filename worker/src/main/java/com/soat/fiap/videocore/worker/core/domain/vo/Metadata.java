package com.soat.fiap.videocore.worker.core.domain.vo;

import java.util.Objects;

/**
 * Objeto de valor que representa metadados de rastreabilidade do processamento.
 */
public record Metadata(String userId, String requestId) {

    public Metadata {
        validate(userId, requestId);
    }

    private static void validate(String userId, String requestId) {
        Objects.requireNonNull(userId, "userId não pode ser nulo");
        Objects.requireNonNull(requestId, "requestId não pode ser nulo");
    }
}
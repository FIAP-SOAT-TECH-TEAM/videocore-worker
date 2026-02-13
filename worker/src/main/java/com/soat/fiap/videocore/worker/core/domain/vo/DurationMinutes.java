package com.soat.fiap.videocore.worker.core.domain.vo;

import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;

/**
 * Objeto de valor que representa o tempo de duração total de um vídeo em minutos.
 */
public record DurationMinutes(long value) {
    public DurationMinutes {
        validate(value);
    }

    private static void validate(long value) {
        if (value <= 0) {
            throw new VideoException("O tempo de duração total do vídeo em minutos deve ser maior que zero");
        }
    }
}

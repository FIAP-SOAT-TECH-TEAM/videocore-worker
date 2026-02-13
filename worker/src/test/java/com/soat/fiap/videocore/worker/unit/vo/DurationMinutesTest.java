package com.soat.fiap.videocore.worker.unit.vo;

import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.worker.core.domain.vo.DurationMinutes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do value object {@link DurationMinutes}.
 */
class DurationMinutesTest {

    @Test
    void shouldCreateValidDurationMinutes() {
        // Arrange
        long value = 10;

        // Act
        DurationMinutes durationMinutes = new DurationMinutes(value);

        // Assert
        assertEquals(10, durationMinutes.value());
    }

    @Test
    void shouldThrowExceptionWhenDurationIsZero() {
        // Arrange
        long value = 0;

        // Act & Assert
        assertThrows(VideoException.class, () -> new DurationMinutes(value));
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNegative() {
        // Arrange
        long value = -1;

        // Act & Assert
        assertThrows(VideoException.class, () -> new DurationMinutes(value));
    }
}
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
        // arrange
        long value = 10;

        // act
        DurationMinutes durationMinutes = new DurationMinutes(value);

        // assert
        assertEquals(10, durationMinutes.value());
    }

    @Test
    void shouldThrowExceptionWhenDurationIsZero() {
        // arrange
        long value = 0;

        // act / assert
        assertThrows(VideoException.class, () -> new DurationMinutes(value));
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNegative() {
        // arrange
        long value = -1;

        // act / assert
        assertThrows(VideoException.class, () -> new DurationMinutes(value));
    }
}
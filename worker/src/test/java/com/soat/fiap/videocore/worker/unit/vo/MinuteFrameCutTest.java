package com.soat.fiap.videocore.worker.unit.vo;

import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.worker.core.domain.vo.MinuteFrameCut;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do value object {@link MinuteFrameCut}.
 */
class MinuteFrameCutTest {

    @Test
    void shouldCreateValidMinuteFrameCut() {
        // arrange
        long value = 1;

        // act
        MinuteFrameCut minuteFrameCut = new MinuteFrameCut(value);

        // assert
        assertEquals(1, minuteFrameCut.value());
    }

    @Test
    void shouldThrowExceptionWhenMinuteFrameCutIsZero() {
        // arrange
        long value = 0;

        // act / assert
        assertThrows(VideoException.class, () -> new MinuteFrameCut(value));
    }

    @Test
    void shouldThrowExceptionWhenMinuteFrameCutIsNegative() {
        // arrange
        long value = -5;

        // act / assert
        assertThrows(VideoException.class, () -> new MinuteFrameCut(value));
    }
}
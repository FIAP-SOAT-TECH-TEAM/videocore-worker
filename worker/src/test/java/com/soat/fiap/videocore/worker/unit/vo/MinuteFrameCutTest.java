package com.soat.fiap.videocore.worker.unit.vo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.worker.core.domain.vo.MinuteFrameCut;

/**
 * Testes unitários do value object {@link MinuteFrameCut}.
 */
class MinuteFrameCutTest {

	@Test
	void shouldCreateValidMinuteFrameCut() {
		// Arrange
		long value = 1;

		// Act
		MinuteFrameCut minuteFrameCut = new MinuteFrameCut(value);

		// Assert
		assertEquals(1, minuteFrameCut.value());
	}

	@Test
	void shouldThrowExceptionWhenMinuteFrameCutIsZero() {
		// Arrange
		long value = 0;

		// Act & Assert
		assertThrows(VideoException.class, () -> new MinuteFrameCut(value));
	}

	@Test
	void shouldThrowExceptionWhenMinuteFrameCutIsNegative() {
		// Arrange
		long value = -5;

		// Act & Assert
		assertThrows(VideoException.class, () -> new MinuteFrameCut(value));
	}
}

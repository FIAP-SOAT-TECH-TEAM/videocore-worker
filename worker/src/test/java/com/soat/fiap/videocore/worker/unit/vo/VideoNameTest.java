package com.soat.fiap.videocore.worker.unit.vo;

import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.worker.core.domain.vo.VideoName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do value object {@link VideoName}.
 */
class VideoNameTest {

    @Test
    void shouldCreateValidVideoName() {
        // Arrange
        String value = "video.mp4";

        // Act
        VideoName videoName = new VideoName(value);

        // Assert
        assertEquals("video.mp4", videoName.value());
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsNull() {
        // Arrange
        String value = null;

        // Act & Assert
        assertThrows(VideoException.class, () -> new VideoName(value));
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsBlank() {
        // Arrange
        String value = "   ";

        // Act & Assert
        assertThrows(VideoException.class, () -> new VideoName(value));
    }
}
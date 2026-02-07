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
        // arrange
        String value = "video.mp4";

        // act
        VideoName videoName = new VideoName(value);

        // assert
        assertEquals("video.mp4", videoName.value());
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsNull() {
        // arrange
        String value = null;

        // act / assert
        assertThrows(VideoException.class, () -> new VideoName(value));
    }

    @Test
    void shouldThrowExceptionWhenVideoNameIsBlank() {
        // arrange
        String value = "   ";

        // act / assert
        assertThrows(VideoException.class, () -> new VideoName(value));
    }
}
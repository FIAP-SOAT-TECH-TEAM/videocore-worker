package com.soat.fiap.videocore.worker.unit.model;

import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.domain.vo.DurationMinutes;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários da entidade {@link Video}.
 */
class VideoTest {

    @Test
    void shouldCreateValidVideo() {
        // Arrange
        Video video = VideoFixture.validVideo();

        // Act
        String name = video.getVideoName();

        // Assert
        assertEquals("video.mp4", name);
    }

    @Test
    void shouldThrowExceptionWhenVideoFileIsNull() {
        // Arrange
        Video video = VideoFixture.validVideo();

        // Act & Assert
        assertThrows(VideoException.class, () -> video.setVideoFile(null));
    }

    @Test
    void shouldSetVideoFile() {
        // Arrange
        Video video = VideoFixture.validVideo();
        File file = new File("new.mp4");

        // Act
        video.setVideoFile(file);

        // Assert
        assertEquals(file, video.getVideoFile());
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNull() {
        // Arrange
        Video video = VideoFixture.validVideo();

        // Act & Assert
        assertThrows(VideoException.class, () -> video.setDurationMinutes(null));
    }

    @Test
    void shouldThrowExceptionWhenFrameCutGreaterThanDuration() {
        // Arrange
        Video video = VideoFixture.validVideo();

        // Act & Assert
        assertThrows(VideoException.class, () ->
                video.setDurationMinutes(new DurationMinutes(1))
        );
    }

    @Test
    void shouldSetDurationMinutes() {
        // Arrange
        Video video = VideoFixture.validVideo();
        DurationMinutes duration = new DurationMinutes(20);

        // Act
        video.setDurationMinutes(duration);

        // Assert
        assertEquals(20, video.getDurationMinutes());
    }

    @Test
    void shouldReturnDefaultExtensionWhenNoExtension() {
        // Arrange
        Video video = new Video(
                new com.soat.fiap.videocore.worker.core.domain.vo.VideoName("video"),
                new File("video"),
                new DurationMinutes(10),
                VideoFixture.inputStream(),
                new com.soat.fiap.videocore.worker.core.domain.vo.MinuteFrameCut(1),
                new com.soat.fiap.videocore.worker.core.domain.vo.Metadata("u", "r")
        );

        // Act
        String ext = video.getVideoExtension();

        // Assert
        assertEquals(".mp4", ext);
    }

    @Test
    void shouldReturnVideoExtension() {
        // Arrange
        Video video = VideoFixture.validVideo();

        // Act
        String ext = video.getVideoExtension();

        // Assert
        assertEquals(".mp4", ext);
    }

    @Test
    void shouldReturnMetadataValues() {
        // Arrange
        Video video = VideoFixture.validVideo();

        // Act
        String userId = video.getUserId();
        String requestId = video.getRequestId();

        // Assert
        assertEquals("user-1", userId);
        assertEquals("request-1", requestId);
    }

    @Test
    void shouldReturnMinuteFrameCut() {
        // Arrange
        Video video = VideoFixture.validVideo();

        // Act
        long cut = video.getMinuteFrameCut();

        // Assert
        assertEquals(1, cut);
    }
}
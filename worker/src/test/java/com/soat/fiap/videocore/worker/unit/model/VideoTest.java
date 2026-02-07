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
        // arrange
        Video video = VideoFixture.validVideo();

        // act
        String name = video.getVideoName();

        // assert
        assertEquals("video.mp4", name);
    }

    @Test
    void shouldThrowExceptionWhenVideoFileIsNull() {
        // arrange
        Video video = VideoFixture.validVideo();

        // act / assert
        assertThrows(VideoException.class, () -> video.setVideoFile(null));
    }

    @Test
    void shouldSetVideoFile() {
        // arrange
        Video video = VideoFixture.validVideo();
        File file = new File("new.mp4");

        // act
        video.setVideoFile(file);

        // assert
        assertEquals(file, video.getVideoFile());
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNull() {
        // arrange
        Video video = VideoFixture.validVideo();

        // act / assert
        assertThrows(VideoException.class, () -> video.setDurationMinutes(null));
    }

    @Test
    void shouldThrowExceptionWhenFrameCutGreaterThanDuration() {
        // arrange
        Video video = VideoFixture.validVideo();

        // act / assert
        assertThrows(VideoException.class, () ->
                video.setDurationMinutes(new DurationMinutes(1))
        );
    }

    @Test
    void shouldSetDurationMinutes() {
        // arrange
        Video video = VideoFixture.validVideo();
        DurationMinutes duration = new DurationMinutes(20);

        // act
        video.setDurationMinutes(duration);

        // assert
        assertEquals(20, video.getDurationMinutes());
    }

    @Test
    void shouldReturnDefaultExtensionWhenNoExtension() {
        // arrange
        Video video = new Video(
                new com.soat.fiap.videocore.worker.core.domain.vo.VideoName("video"),
                new File("video"),
                new DurationMinutes(10),
                VideoFixture.inputStream(),
                new com.soat.fiap.videocore.worker.core.domain.vo.MinuteFrameCut(1),
                new com.soat.fiap.videocore.worker.core.domain.vo.Metadata("u", "r")
        );

        // act
        String ext = video.getVideoExtension();

        // assert
        assertEquals(".mp4", ext);
    }

    @Test
    void shouldReturnVideoExtension() {
        // arrange
        Video video = VideoFixture.validVideo();

        // act
        String ext = video.getVideoExtension();

        // assert
        assertEquals(".mp4", ext);
    }

    @Test
    void shouldReturnMetadataValues() {
        // arrange
        Video video = VideoFixture.validVideo();

        // act
        String userId = video.getUserId();
        String requestId = video.getRequestId();

        // assert
        assertEquals("user-1", userId);
        assertEquals("request-1", requestId);
    }

    @Test
    void shouldReturnMinuteFrameCut() {
        // arrange
        Video video = VideoFixture.validVideo();

        // act
        long cut = video.getMinuteFrameCut();

        // assert
        assertEquals(1, cut);
    }
}
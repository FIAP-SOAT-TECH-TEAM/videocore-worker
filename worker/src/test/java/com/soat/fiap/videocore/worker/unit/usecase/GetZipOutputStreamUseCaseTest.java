package com.soat.fiap.videocore.worker.unit.usecase;

import com.soat.fiap.videocore.worker.core.application.usecase.GetZipOutputStreamUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.VideoGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;
import org.junit.jupiter.api.Test;

import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link GetZipOutputStreamUseCase}.
 */
class GetZipOutputStreamUseCaseTest {

    @Test
    void shouldReturnZipOutputStream() {
        // arrange
        VideoGateway gateway = mock(VideoGateway.class);
        ZipOutputStream zos = mock(ZipOutputStream.class);
        when(gateway.getZipOutputStream(any())).thenReturn(zos);
        GetZipOutputStreamUseCase useCase = new GetZipOutputStreamUseCase(gateway);
        Video video = VideoFixture.validVideo();

        // act
        ZipOutputStream result = useCase.getZipOutputStream(video);

        // assert
        assertEquals(zos, result);
    }

    @Test
    void shouldThrowExceptionWhenVideoIsNull() {
        // arrange
        GetZipOutputStreamUseCase useCase =
                new GetZipOutputStreamUseCase(mock(VideoGateway.class));

        // act / assert
        assertThrows(ProcessVideoException.class, () -> useCase.getZipOutputStream(null));
    }

    @Test
    void shouldThrowExceptionWhenZipIsNull() {
        // arrange
        VideoGateway gateway = mock(VideoGateway.class);
        when(gateway.getZipOutputStream(any())).thenReturn(null);
        GetZipOutputStreamUseCase useCase = new GetZipOutputStreamUseCase(gateway);
        Video video = VideoFixture.validVideo();

        // act / assert
        assertThrows(ProcessVideoException.class, () -> useCase.getZipOutputStream(video));
    }
}
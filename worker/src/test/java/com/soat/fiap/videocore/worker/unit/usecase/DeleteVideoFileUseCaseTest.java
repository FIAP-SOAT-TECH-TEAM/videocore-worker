package com.soat.fiap.videocore.worker.unit.usecase;

import com.soat.fiap.videocore.worker.core.application.usecase.DeleteVideoFileUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.FileGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link DeleteVideoFileUseCase}.
 */
class DeleteVideoFileUseCaseTest {

    @Test
    void shouldDeleteVideoFileWhenPresent() throws IOException {
        // arrange
        FileGateway fileGateway = mock(FileGateway.class);
        DeleteVideoFileUseCase useCase = new DeleteVideoFileUseCase(fileGateway);
        Video video = VideoFixture.validVideo();

        // act
        useCase.deleteVideoFile(video);

        // assert
        verify(fileGateway).deleteFileIfExists(video.getVideoFile());
    }

    @Test
    void shouldNotFailWhenVideoIsNull() {
        // arrange
        FileGateway fileGateway = mock(FileGateway.class);
        DeleteVideoFileUseCase useCase = new DeleteVideoFileUseCase(fileGateway);

        // act
        useCase.deleteVideoFile(null);

        // assert
        verifyNoInteractions(fileGateway);
    }

    @Test
    void shouldThrowProcessVideoExceptionWhenGatewayFails() throws IOException {
        // arrange
        FileGateway fileGateway = mock(FileGateway.class);
        doThrow(new RuntimeException()).when(fileGateway).deleteFileIfExists(any());
        DeleteVideoFileUseCase useCase = new DeleteVideoFileUseCase(fileGateway);
        Video video = VideoFixture.validVideo();

        // act / assert
        assertThrows(ProcessVideoException.class, () -> useCase.deleteVideoFile(video));
    }
}
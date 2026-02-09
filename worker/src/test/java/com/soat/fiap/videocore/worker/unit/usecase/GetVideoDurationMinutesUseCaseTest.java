package com.soat.fiap.videocore.worker.unit.usecase;

import com.soat.fiap.videocore.worker.core.application.usecase.GetVideoDurationMinutesUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.ProcessVideoGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link GetVideoDurationMinutesUseCase}.
 */
class GetVideoDurationMinutesUseCaseTest {

    @Test
    void shouldSetVideoDurationMinutes() {
        // Arrange
        ProcessVideoGateway gateway = mock(ProcessVideoGateway.class);
        when(gateway.getVideoDurationMinutes(any())).thenReturn(20L);
        GetVideoDurationMinutesUseCase useCase = new GetVideoDurationMinutesUseCase(gateway);
        Video video = VideoFixture.validVideo();

        // Act
        useCase.getVideoDurationMinutes(video);

        // Assert
        assertEquals(20, video.getDurationMinutes());
    }

    @Test
    void shouldThrowExceptionWhenVideoIsNull() {
        // Arrange
        GetVideoDurationMinutesUseCase useCase =
                new GetVideoDurationMinutesUseCase(mock(ProcessVideoGateway.class));

        // Act & Assert
        assertThrows(ProcessVideoException.class, () -> useCase.getVideoDurationMinutes(null));
    }

    @Test
    void shouldThrowExceptionWhenGatewayFails() {
        // Arrange
        ProcessVideoGateway gateway = mock(ProcessVideoGateway.class);
        when(gateway.getVideoDurationMinutes(any())).thenThrow(IllegalStateException.class);
        GetVideoDurationMinutesUseCase useCase = new GetVideoDurationMinutesUseCase(gateway);
        Video video = VideoFixture.validVideo();

        // Act & Assert
        assertThrows(ProcessVideoException.class, () -> useCase.getVideoDurationMinutes(video));
    }
}
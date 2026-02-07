package com.soat.fiap.videocore.worker.unit.usecase;

import com.soat.fiap.videocore.worker.core.application.usecase.GetVideoUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoNotFoundException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.VideoGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link GetVideoUseCase}.
 */
class GetVideoUseCaseTest {

    @Test
    void shouldReturnVideo() {
        // arrange
        VideoGateway gateway = mock(VideoGateway.class);
        Video video = VideoFixture.validVideo();
        when(gateway.getVideo(any())).thenReturn(video);
        GetVideoUseCase useCase = new GetVideoUseCase(gateway);

        // act
        Video result = useCase.getVideo("url");

        // assert
        assertEquals(video, result);
    }

    @Test
    void shouldThrowExceptionWhenUrlIsBlank() {
        // arrange
        GetVideoUseCase useCase = new GetVideoUseCase(mock(VideoGateway.class));

        // act / assert
        assertThrows(VideoException.class, () -> useCase.getVideo(" "));
    }

    @Test
    void shouldThrowNotFoundWhenVideoIsNull() {
        // arrange
        VideoGateway gateway = mock(VideoGateway.class);
        when(gateway.getVideo(any())).thenReturn(null);
        GetVideoUseCase useCase = new GetVideoUseCase(gateway);

        // act / assert
        assertThrows(VideoNotFoundException.class, () -> useCase.getVideo("url"));
    }
}
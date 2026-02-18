package com.soat.fiap.videocore.worker.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.worker.core.application.usecase.GetVideoUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoNotFoundException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.VideoGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;

/**
 * Testes unitários do {@link GetVideoUseCase}.
 */
class GetVideoUseCaseTest {

	@Test
	void shouldReturnVideo() {
		// Arrange
		VideoGateway gateway = mock(VideoGateway.class);
		Video video = VideoFixture.validVideo();
		when(gateway.getVideo(any())).thenReturn(video);
		GetVideoUseCase useCase = new GetVideoUseCase(gateway);

		// Act
		Video result = useCase.getVideo("url");

		// Assert
		assertEquals(video, result);
	}

	@Test
	void shouldThrowExceptionWhenUrlIsBlank() {
		// Arrange
		GetVideoUseCase useCase = new GetVideoUseCase(mock(VideoGateway.class));

		// Act & Assert
		assertThrows(VideoException.class, () -> useCase.getVideo(" "));
	}

	@Test
	void shouldThrowNotFoundWhenVideoIsNull() {
		// Arrange
		VideoGateway gateway = mock(VideoGateway.class);
		when(gateway.getVideo(any())).thenReturn(null);
		GetVideoUseCase useCase = new GetVideoUseCase(gateway);

		// Act & Assert
		assertThrows(VideoNotFoundException.class, () -> useCase.getVideo("url"));
	}
}

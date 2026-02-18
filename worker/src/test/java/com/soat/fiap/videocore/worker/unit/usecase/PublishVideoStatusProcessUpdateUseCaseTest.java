package com.soat.fiap.videocore.worker.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.worker.core.application.usecase.PublishVideoStatusProcessUpdateUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.EventPublisherGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;

/**
 * Testes unitários do {@link PublishVideoStatusProcessUpdateUseCase}.
 */
class PublishVideoStatusProcessUpdateUseCaseTest {

	@Test
	void shouldPublishEvent() {
		// Arrange
		EventPublisherGateway gateway = mock(EventPublisherGateway.class);
		PublishVideoStatusProcessUpdateUseCase useCase = new PublishVideoStatusProcessUpdateUseCase(gateway);
		Video video = VideoFixture.validVideo();

		// Act
		useCase.publishVideoStatusProcessUpdate(video, 10.0, 1L, false);

		// Assert
		verify(gateway).publishVideoStatusProcessUpdateEvent(any());
	}

	@Test
	void shouldThrowExceptionWhenVideoIsNull() {
		// Arrange
		PublishVideoStatusProcessUpdateUseCase useCase = new PublishVideoStatusProcessUpdateUseCase(
				mock(EventPublisherGateway.class));

		// Act & Assert
		assertThrows(ProcessVideoException.class, () -> useCase.publishVideoStatusProcessUpdate(null, 10.0, 1L, false));
	}
}

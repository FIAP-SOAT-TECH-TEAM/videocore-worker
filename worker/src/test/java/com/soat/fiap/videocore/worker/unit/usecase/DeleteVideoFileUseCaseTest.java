package com.soat.fiap.videocore.worker.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.worker.core.application.usecase.DeleteVideoFileUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.FileGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;

/**
 * Testes unitários do {@link DeleteVideoFileUseCase}.
 */
class DeleteVideoFileUseCaseTest {

	@Test
	void shouldDeleteVideoFileWhenPresent() throws IOException {
		// Arrange
		FileGateway fileGateway = mock(FileGateway.class);
		DeleteVideoFileUseCase useCase = new DeleteVideoFileUseCase(fileGateway);
		Video video = VideoFixture.validVideo();

		// Act
		useCase.deleteVideoFile(video);

		// Assert
		verify(fileGateway).deleteFileIfExists(video.getVideoFile());
	}

	@Test
	void shouldNotFailWhenVideoIsNull() {
		// Arrange
		FileGateway fileGateway = mock(FileGateway.class);
		DeleteVideoFileUseCase useCase = new DeleteVideoFileUseCase(fileGateway);

		// Act
		useCase.deleteVideoFile(null);

		// Assert
		verifyNoInteractions(fileGateway);
	}

	@Test
	void shouldThrowProcessVideoExceptionWhenGatewayFails() throws IOException {
		// Arrange
		FileGateway fileGateway = mock(FileGateway.class);
		doThrow(new RuntimeException()).when(fileGateway).deleteFileIfExists(any());
		DeleteVideoFileUseCase useCase = new DeleteVideoFileUseCase(fileGateway);
		Video video = VideoFixture.validVideo();

		// Act & Assert
		assertThrows(ProcessVideoException.class, () -> useCase.deleteVideoFile(video));
	}
}

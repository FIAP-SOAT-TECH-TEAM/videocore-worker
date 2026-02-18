package com.soat.fiap.videocore.worker.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.worker.core.application.usecase.DownloadVideoToTempFileUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.FileGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;

/**
 * Testes unitários do {@link DownloadVideoToTempFileUseCase}.
 */
class DownloadVideoToTempFileUseCaseTest {

	@Test
	void shouldDownloadVideoToTempFile() throws IOException {
		// Arrange
		FileGateway fileGateway = mock(FileGateway.class);
		File file = new File("temp.mp4");
		when(fileGateway.downloadTempFileFromInputStream(any(), any(), any())).thenReturn(file);
		DownloadVideoToTempFileUseCase useCase = new DownloadVideoToTempFileUseCase(fileGateway);
		Video video = VideoFixture.validVideo();

		// Act
		useCase.downloadVideoToTempFile(video);

		// Assert
		assertEquals(file, video.getVideoFile());
	}

	@Test
	void shouldThrowExceptionWhenVideoIsNull() {
		// Arrange
		DownloadVideoToTempFileUseCase useCase = new DownloadVideoToTempFileUseCase(mock(FileGateway.class));

		// Act & Assert
		assertThrows(ProcessVideoException.class, () -> useCase.downloadVideoToTempFile(null));
	}

	@Test
	void shouldThrowExceptionWhenIOExceptionOccurs() throws IOException {
		// Arrange
		FileGateway fileGateway = mock(FileGateway.class);
		when(fileGateway.downloadTempFileFromInputStream(any(), any(), any())).thenThrow(new IOException());
		DownloadVideoToTempFileUseCase useCase = new DownloadVideoToTempFileUseCase(fileGateway);
		Video video = VideoFixture.validVideo();

		// Act & Assert
		assertThrows(ProcessVideoException.class, () -> useCase.downloadVideoToTempFile(video));
	}
}

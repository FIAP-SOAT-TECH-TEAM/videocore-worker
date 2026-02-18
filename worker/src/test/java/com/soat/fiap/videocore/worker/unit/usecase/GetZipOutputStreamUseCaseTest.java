package com.soat.fiap.videocore.worker.unit.usecase;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.zip.ZipOutputStream;

import org.junit.jupiter.api.Test;

import com.soat.fiap.videocore.worker.core.application.usecase.GetZipOutputStreamUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.VideoGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;

/**
 * Testes unitários do {@link GetZipOutputStreamUseCase}.
 */
class GetZipOutputStreamUseCaseTest {

	@Test
	void shouldReturnZipOutputStream() {
		// Arrange
		VideoGateway gateway = mock(VideoGateway.class);
		ZipOutputStream zos = mock(ZipOutputStream.class);
		when(gateway.getZipOutputStream(any())).thenReturn(zos);
		GetZipOutputStreamUseCase useCase = new GetZipOutputStreamUseCase(gateway);
		Video video = VideoFixture.validVideo();

		// Act
		ZipOutputStream result = useCase.getZipOutputStream(video);

		// Assert
		assertEquals(zos, result);
	}

	@Test
	void shouldThrowExceptionWhenVideoIsNull() {
		// Arrange
		GetZipOutputStreamUseCase useCase = new GetZipOutputStreamUseCase(mock(VideoGateway.class));

		// Act & Assert
		assertThrows(ProcessVideoException.class, () -> useCase.getZipOutputStream(null));
	}

	@Test
	void shouldThrowExceptionWhenZipIsNull() {
		// Arrange
		VideoGateway gateway = mock(VideoGateway.class);
		when(gateway.getZipOutputStream(any())).thenReturn(null);
		GetZipOutputStreamUseCase useCase = new GetZipOutputStreamUseCase(gateway);
		Video video = VideoFixture.validVideo();

		// Act & Assert
		assertThrows(ProcessVideoException.class, () -> useCase.getZipOutputStream(video));
	}
}

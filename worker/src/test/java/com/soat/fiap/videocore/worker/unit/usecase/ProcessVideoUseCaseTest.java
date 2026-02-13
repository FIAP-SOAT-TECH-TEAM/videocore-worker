package com.soat.fiap.videocore.worker.unit.usecase;

import com.soat.fiap.videocore.worker.core.application.input.VideoProcessMetadataInput;
import com.soat.fiap.videocore.worker.core.application.usecase.ProcessVideoUseCase;
import com.soat.fiap.videocore.worker.core.application.usecase.PublishVideoStatusProcessUpdateUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.ProcessVideoGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link ProcessVideoUseCase}.
 */
class ProcessVideoUseCaseTest {

    @Test
    void shouldProcessVideoSuccessfully() throws IOException {
        // Arrange
        ProcessVideoGateway gateway = mock(ProcessVideoGateway.class);
        PublishVideoStatusProcessUpdateUseCase publishUseCase =
                mock(PublishVideoStatusProcessUpdateUseCase.class);
        ProcessVideoUseCase useCase = new ProcessVideoUseCase(publishUseCase, gateway);
        Video video = VideoFixture.validVideo();
        ZipOutputStream zos = mock(ZipOutputStream.class);
        VideoProcessMetadataInput metadata = new VideoProcessMetadataInput();

        // Act
        useCase.processVideo(video, zos, metadata);

        // Assert
        verify(gateway, atLeastOnce()).processVideo(any(), anyLong(), any(), any());
    }

    @Test
    void shouldThrowExceptionWhenVideoOrStreamIsNull() {
        // Arrange
        ProcessVideoUseCase useCase =
                new ProcessVideoUseCase(mock(PublishVideoStatusProcessUpdateUseCase.class),
                        mock(ProcessVideoGateway.class));

        // Act & Assert
        assertThrows(ProcessVideoException.class,
                () -> useCase.processVideo(null, null, new VideoProcessMetadataInput()));
    }
}
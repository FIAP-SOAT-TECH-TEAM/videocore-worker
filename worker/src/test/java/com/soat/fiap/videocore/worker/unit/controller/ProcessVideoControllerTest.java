package com.soat.fiap.videocore.worker.unit.controller;

import com.soat.fiap.videocore.worker.core.application.input.VideoProcessMetadataInput;
import com.soat.fiap.videocore.worker.core.application.usecase.*;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.controller.ProcessVideoController;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;
import com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.payload.BlobCreatedCloudEventSchemaPayload;
import org.junit.jupiter.api.Test;

import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link ProcessVideoController}.
 */
class ProcessVideoControllerTest {

    @Test
    void shouldProcessVideoSuccessfully() {
        // Arrange
        GetVideoUseCase getVideoUseCase = mock(GetVideoUseCase.class);
        GetZipOutputStreamUseCase getZipOutputStreamUseCase = mock(GetZipOutputStreamUseCase.class);
        DownloadVideoToTempFileUseCase downloadVideoToTempFileUseCase = mock(DownloadVideoToTempFileUseCase.class);
        GetVideoDurationMinutesUseCase getVideoDurationMinutesUseCase = mock(GetVideoDurationMinutesUseCase.class);
        ProcessVideoUseCase processVideoUseCase = mock(ProcessVideoUseCase.class);
        DeleteVideoFileUseCase deleteVideoFileUseCase = mock(DeleteVideoFileUseCase.class);
        PublishVideoStatusProcessUpdateUseCase publishVideoStatusProcessUpdateUseCase =
                mock(PublishVideoStatusProcessUpdateUseCase.class);

        ProcessVideoController controller = new ProcessVideoController(
                getVideoUseCase,
                getZipOutputStreamUseCase,
                downloadVideoToTempFileUseCase,
                getVideoDurationMinutesUseCase,
                processVideoUseCase,
                deleteVideoFileUseCase,
                publishVideoStatusProcessUpdateUseCase
        );

        Video video = VideoFixture.validVideo();
        ZipOutputStream zipOutputStream = mock(ZipOutputStream.class);

        BlobCreatedCloudEventSchemaPayload payload = mock(BlobCreatedCloudEventSchemaPayload.class);
        BlobCreatedCloudEventSchemaPayload.DataPayload data = mock(BlobCreatedCloudEventSchemaPayload.DataPayload.class);

        when(payload.data()).thenReturn(data);
        when(data.url()).thenReturn("video-url");
        when(getVideoUseCase.getVideo(any())).thenReturn(video);
        when(getZipOutputStreamUseCase.getZipOutputStream(video)).thenReturn(zipOutputStream);

        // Act
        controller.processVideo(payload);

        // Assert
        verify(getVideoUseCase).getVideo("video-url");
        verify(downloadVideoToTempFileUseCase).downloadVideoToTempFile(video);
        verify(getVideoDurationMinutesUseCase).getVideoDurationMinutes(video);
        verify(getZipOutputStreamUseCase).getZipOutputStream(video);
        verify(processVideoUseCase)
                .processVideo(eq(video), eq(zipOutputStream), any(VideoProcessMetadataInput.class));
        verify(deleteVideoFileUseCase).deleteVideoFile(video);
        verifyNoInteractions(publishVideoStatusProcessUpdateUseCase);
    }

    @Test
    void shouldPublishErrorEventAndDeleteFileWhenExceptionOccurs() {
        // Arrange
        GetVideoUseCase getVideoUseCase = mock(GetVideoUseCase.class);
        GetZipOutputStreamUseCase getZipOutputStreamUseCase = mock(GetZipOutputStreamUseCase.class);
        DownloadVideoToTempFileUseCase downloadVideoToTempFileUseCase = mock(DownloadVideoToTempFileUseCase.class);
        GetVideoDurationMinutesUseCase getVideoDurationMinutesUseCase = mock(GetVideoDurationMinutesUseCase.class);
        ProcessVideoUseCase processVideoUseCase = mock(ProcessVideoUseCase.class);
        DeleteVideoFileUseCase deleteVideoFileUseCase = mock(DeleteVideoFileUseCase.class);
        PublishVideoStatusProcessUpdateUseCase publishVideoStatusProcessUpdateUseCase =
                mock(PublishVideoStatusProcessUpdateUseCase.class);

        ProcessVideoController controller = new ProcessVideoController(
                getVideoUseCase,
                getZipOutputStreamUseCase,
                downloadVideoToTempFileUseCase,
                getVideoDurationMinutesUseCase,
                processVideoUseCase,
                deleteVideoFileUseCase,
                publishVideoStatusProcessUpdateUseCase
        );

        Video video = VideoFixture.validVideo();

        BlobCreatedCloudEventSchemaPayload payload = mock(BlobCreatedCloudEventSchemaPayload.class);
        BlobCreatedCloudEventSchemaPayload.DataPayload data = mock(BlobCreatedCloudEventSchemaPayload.DataPayload.class);

        when(payload.data()).thenReturn(data);
        when(data.url()).thenReturn("video-url");
        when(getVideoUseCase.getVideo(any())).thenReturn(video);
        doThrow(new RuntimeException("erro"))
                .when(downloadVideoToTempFileUseCase).downloadVideoToTempFile(video);

        // Act
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> controller.processVideo(payload)
        );

        // Assert
        assertEquals("erro", exception.getMessage());
        verify(publishVideoStatusProcessUpdateUseCase)
                .publishVideoStatusProcessUpdate(eq(video), any(), anyLong(), eq(true));
        verify(deleteVideoFileUseCase).deleteVideoFile(video);
    }

    @Test
    void shouldPublishErrorEventWhenExceptionOccursBeforeVideoIsLoaded() {
        // Arrange
        GetVideoUseCase getVideoUseCase = mock(GetVideoUseCase.class);
        GetZipOutputStreamUseCase getZipOutputStreamUseCase = mock(GetZipOutputStreamUseCase.class);
        DownloadVideoToTempFileUseCase downloadVideoToTempFileUseCase = mock(DownloadVideoToTempFileUseCase.class);
        GetVideoDurationMinutesUseCase getVideoDurationMinutesUseCase = mock(GetVideoDurationMinutesUseCase.class);
        ProcessVideoUseCase processVideoUseCase = mock(ProcessVideoUseCase.class);
        DeleteVideoFileUseCase deleteVideoFileUseCase = mock(DeleteVideoFileUseCase.class);
        PublishVideoStatusProcessUpdateUseCase publishVideoStatusProcessUpdateUseCase =
                mock(PublishVideoStatusProcessUpdateUseCase.class);

        ProcessVideoController controller = new ProcessVideoController(
                getVideoUseCase,
                getZipOutputStreamUseCase,
                downloadVideoToTempFileUseCase,
                getVideoDurationMinutesUseCase,
                processVideoUseCase,
                deleteVideoFileUseCase,
                publishVideoStatusProcessUpdateUseCase
        );

        BlobCreatedCloudEventSchemaPayload payload = mock(BlobCreatedCloudEventSchemaPayload.class);
        BlobCreatedCloudEventSchemaPayload.DataPayload data = mock(BlobCreatedCloudEventSchemaPayload.DataPayload.class);

        when(payload.data()).thenReturn(data);
        when(data.url()).thenReturn("video-url");
        when(getVideoUseCase.getVideo(any())).thenThrow(new RuntimeException("erro"));

        // Act
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> controller.processVideo(payload)
        );

        // Assert
        assertEquals("erro", exception.getMessage());
        verify(publishVideoStatusProcessUpdateUseCase)
                .publishVideoStatusProcessUpdate(isNull(), any(), anyLong(), eq(true));
        verify(deleteVideoFileUseCase).deleteVideoFile(null);
    }
}
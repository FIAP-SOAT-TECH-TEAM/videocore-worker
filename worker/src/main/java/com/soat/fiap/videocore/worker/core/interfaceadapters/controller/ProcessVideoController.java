package com.soat.fiap.videocore.worker.core.interfaceadapters.controller;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.application.input.VideoProcessMetadataInput;
import com.soat.fiap.videocore.worker.core.application.usecase.*;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.payload.BlobCreatedCloudEventSchemaPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Controller responsável por orquestrar o processamento de vídeos
 * a partir de eventos de criação de blob.
 */
@Component
@RequiredArgsConstructor
public class ProcessVideoController {

    private final GetVideoUseCase getVideoUseCase;
    private final GetZipOutputStreamUseCase getZipOutputStreamUseCase;
    private final DownloadVideoToTempFileUseCase downloadVideoToTempFileUseCase;
    private final GetVideoDurationMinutesUseCase getVideoDurationMinutesUseCase;
    private final ProcessVideoUseCase processVideoUseCase;
    private final DeleteVideoFileUseCase deleteVideoFileUseCase;
    private final PublishVideoStatusProcessUpdateUseCase publishVideoStatusProcessUpdateUseCase;

    /**
     * Orquestra o fluxo de processamento de um vídeo recém-criado no blob storage. Capturando suas imagens e enviando para um destino
     *
     * @param payload evento CloudEvent contendo dados do blob
     */
    @WithSpan(name = "controller.process.video")
    public void processVideo(BlobCreatedCloudEventSchemaPayload payload) {
        Video video = null;
        var videoProcessMetadata = new VideoProcessMetadataInput();

        try {
            var videoUrl = payload.data().url();
            video = getVideoUseCase.getVideo(videoUrl);

            downloadVideoToTempFileUseCase.downloadVideoToTempFile(video);
            getVideoDurationMinutesUseCase.getVideoDurationMinutes(video);
            var zipOutputStream = getZipOutputStreamUseCase.getZipOutputStream(video);

            processVideoUseCase.processVideo(video, zipOutputStream, videoProcessMetadata);
        }
        catch (Exception e) {
            publishVideoStatusProcessUpdateUseCase.publishVideoStatusProcessUpdate(video, videoProcessMetadata.getCurrentPercent(), videoProcessMetadata.getImageMinute(), true);
            throw e;
        }
        finally {
            deleteVideoFileUseCase.deleteVideoFile(video);
        }
    }
}
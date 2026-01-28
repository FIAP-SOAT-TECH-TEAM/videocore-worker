package com.soat.fiap.videocore.worker.core.interfaceadapters.controller;

import com.soat.fiap.videocore.worker.core.application.usecase.*;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.infrastructure.in.event.listener.azsvcbus.dto.BlobCreatedCloudEventSchema;
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

    /**
     * Orquestra o fluxo de processamento de um vídeo recém-criado no blob storage. Capturando suas imagens e enviando para um destino
     *
     * @param blobCreatedCloudEventSchema evento CloudEvent contendo dados do blob
     */
    public void processVideo(BlobCreatedCloudEventSchema blobCreatedCloudEventSchema) {
        Video video = null;

        try {
            var videoUrl = blobCreatedCloudEventSchema.getData().getUrl();
            video = getVideoUseCase.getVideo(videoUrl);

            downloadVideoToTempFileUseCase.downloadVideoToTempFile(video);
            getVideoDurationMinutesUseCase.getVideoDurationMinutes(video);
            var zipOutputStream = getZipOutputStreamUseCase.getZipOutputStream(video);

            processVideoUseCase.processVideo(video, zipOutputStream);
        }
        finally {
            deleteVideoFileUseCase.deleteVideoFile(video);
        }
    }
}
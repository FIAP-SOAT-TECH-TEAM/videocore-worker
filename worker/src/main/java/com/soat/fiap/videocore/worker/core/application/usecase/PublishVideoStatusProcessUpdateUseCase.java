package com.soat.fiap.videocore.worker.core.application.usecase;

import com.soat.fiap.videocore.worker.core.domain.event.ProcessVideoStatusUpdateEvent;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.EventPublisherGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Caso de uso responsável por publicar eventos de atualização do status de processamento de um vídeo.
 */
@Component
@RequiredArgsConstructor
public class PublishVideoStatusProcessUpdateUseCase {

    /** Gateway para publicação do evento de atualização de status. */
    private final EventPublisherGateway eventPublisherGateway;

    /**
     * Publica um evento de atualização do status de processamento do vídeo.
     *
     * @param video           vídeo em processamento
     * @param currentPercent  percentual atual do processamento
     */
    public void publishVideoStatusProcessUpdate(Video video, Double currentPercent) {
        var updateStatusEvent = new ProcessVideoStatusUpdateEvent(
                video.getVideoName(),
                video.getUserId(),
                video.getRequestId(),
                video.getDurationMinutes(),
                video.getMinuteFrameCut(),
                currentPercent,
                Instant.now()
        );

        eventPublisherGateway.publishVideoStatusProcessUpdateEvent(updateStatusEvent);
    }
}
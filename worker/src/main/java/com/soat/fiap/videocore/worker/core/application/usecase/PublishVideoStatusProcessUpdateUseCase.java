package com.soat.fiap.videocore.worker.core.application.usecase;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.event.ProcessVideoStatusUpdateEvent;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
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

    private final EventPublisherGateway eventPublisherGateway;

    /**
     * Publica um evento de atualização do status de processamento do vídeo.
     *
     * @param video           vídeo em processamento
     * @param currentPercent  percentual atual do processamento
     * @param imageMinute     Minuto em que a imagem foi capturada
     */
    @WithSpan(name = "usecase.publish.video.status.update.event")
    public void publishVideoStatusProcessUpdate(Video video, Double currentPercent, long imageMinute) {
        if (video == null)
            throw new ProcessVideoException("O video não pode ser nulo para publicação de evento");

        var event = new ProcessVideoStatusUpdateEvent(
                video.getVideoName(),
                video.getUserId(),
                video.getRequestId(),
                imageMinute,
                video.getMinuteFrameCut(),
                currentPercent,
                Instant.now()
        );

        eventPublisherGateway.publishVideoStatusProcessUpdateEvent(event);
    }
}
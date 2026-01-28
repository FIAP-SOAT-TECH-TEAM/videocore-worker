package com.soat.fiap.videocore.worker.core.interfaceadapters.gateway;

import com.soat.fiap.videocore.worker.common.observability.trace.TraceContext;
import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.event.ProcessVideoStatusUpdateEvent;
import com.soat.fiap.videocore.worker.core.interfaceadapters.mapper.EventMapper;
import com.soat.fiap.videocore.worker.infrastructure.common.source.EventPublisherSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Gateway responsável pela publicação de eventos de domínio.
 */
@Component
@RequiredArgsConstructor
public class EventPublisherGateway {

    private final EventPublisherSource eventPublisherSource;
    private final EventMapper eventMapper;

    /**
     * Publica um evento de atualização do status do processamento de vídeo.
     *
     * @param event evento de atualização de status a ser publicado
     */
    @WithSpan(name = "process.video.publish-process-update-status-event")
    public void publishVideoStatusProcessUpdateEvent(ProcessVideoStatusUpdateEvent event) {
        var dto = eventMapper.toDto(event);
        eventPublisherSource.publishVideoStatusProcessUpdateEvent(dto);

        TraceContext.addEvent("event.object", event);
    }
}
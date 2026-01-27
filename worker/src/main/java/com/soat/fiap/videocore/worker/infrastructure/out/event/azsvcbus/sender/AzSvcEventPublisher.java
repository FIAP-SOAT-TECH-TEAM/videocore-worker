package com.soat.fiap.videocore.worker.infrastructure.out.event.azsvcbus.sender;

import com.azure.spring.cloud.service.servicebus.properties.ServiceBusEntityType;
import com.azure.spring.messaging.servicebus.core.ServiceBusTemplate;
import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.ProcessVideoStatusUpdateEventDto;
import com.soat.fiap.videocore.worker.infrastructure.common.event.EventMessagingChannel;
import com.soat.fiap.videocore.worker.infrastructure.common.source.EventPublisherSource;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * Implementação de {@link EventPublisherSource} para publicação de eventos no Azure Service Bus.
 */
@Component
@RequiredArgsConstructor
public class AzSvcEventPublisher implements EventPublisherSource {

    private final ServiceBusTemplate serviceBusTemplate;

    /**
     * Publica um evento de atualização de status de vídeo no canal de mensageria correspondente.
     *
     * @param event DTO contendo informações do evento de atualização
     */
    @Override
    public void publishVideoStatusProcessUpdateEvent(ProcessVideoStatusUpdateEventDto event) {
        var payload = MessageBuilder.withPayload(event).build();

        serviceBusTemplate.setDefaultEntityType(ServiceBusEntityType.TOPIC);
        serviceBusTemplate.send(EventMessagingChannel.PROCESS_STATUS_TOPIC, payload);
    }
}
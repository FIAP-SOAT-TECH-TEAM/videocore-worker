package com.soat.fiap.videocore.worker.infrastructure.out.event.azsvcbus.sender;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.ProcessVideoStatusUpdateEventDto;
import com.soat.fiap.videocore.worker.infrastructure.common.source.EventPublisherSource;
import com.soat.fiap.videocore.worker.infrastructure.common.exceptions.azure.svcbus.ServiceBusSerializationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Implementação de {@link EventPublisherSource} para publicação de eventos no Azure Service Bus.
 */
@Component
@RequiredArgsConstructor
public class AzSvcEventSender implements EventPublisherSource {

    private final ServiceBusSenderClient processStatusSender;
    private final ObjectMapper objectMapper;

    /**
     * Publica um evento de atualização de status de vídeo no canal de mensageria correspondente.
     *
     * @param event Evento a ser publicado.
     */
    @Override
    public void publishVideoStatusProcessUpdateEvent(ProcessVideoStatusUpdateEventDto event) {
        try {
            var rawEvent = objectMapper.writeValueAsString(event);
            var message = new ServiceBusMessage(rawEvent);

            processStatusSender.sendMessage(message);
        }
        catch (JsonProcessingException e) {
            throw new ServiceBusSerializationException("Erro ao serializar evento", e);
        }
    }
}
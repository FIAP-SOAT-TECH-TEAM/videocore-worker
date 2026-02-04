package com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.listener;

import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.spring.messaging.servicebus.implementation.core.annotation.ServiceBusListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soat.fiap.videocore.worker.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.interfaceadapters.controller.ProcessVideoController;
import com.soat.fiap.videocore.worker.infrastructure.common.event.EventMessagingChannel;
import com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.payload.BlobCreatedCloudEventSchemaPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Listener responsável por consumir eventos da fila de processamento.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProcessListener {

    private final ObjectMapper objectMapper;
    private final ProcessVideoController processVideoController;

    /**
     * Consome mensagens da fila {@link EventMessagingChannel#PROCESS_QUEUE}.
     *
     * @param message Wrapper da mensagem recebida
     */
    @WithSpan(name = "process.video.event")
    @ServiceBusListener(destination = EventMessagingChannel.PROCESS_QUEUE)
    public void processEvent(ServiceBusReceivedMessage message) throws Exception {
        try {
            var rawBody = message.getBody().toString();
            CanonicalContext.add("event_object_string", rawBody);

            var body = objectMapper.readValue(rawBody, BlobCreatedCloudEventSchemaPayload.class);
            CanonicalContext.add("event_object", body);

            processVideoController.processVideo(body);

            log.info("request_completed");
        } finally {
            CanonicalContext.clear();
        }
    }
}
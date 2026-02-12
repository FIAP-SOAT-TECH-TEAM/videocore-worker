package com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.listener;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import com.soat.fiap.videocore.worker.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.worker.infrastructure.common.config.azure.svcbus.ServiceBusConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura o processor client para consumir eventos de processamento de vídeo do Service Bus.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class ProcessConfig {

    private final ProcessHandler handler;

    /**
     * Cria um ServiceBusProcessorClient para a fila de processamento.
     *
     * @param builder builder do Service Bus
     * @return client configurado
     */
    @Bean
    public ServiceBusProcessorClient processVideo(ServiceBusClientBuilder builder) {
        return builder.processor()
                .queueName(ServiceBusConfig.PROCESS_QUEUE)
                .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
                .processMessage(handler::handleMessage)
                .processError(errorContext -> {
                    log.error("request_error", errorContext.getException());
                })
                .buildProcessorClient();
    }
}
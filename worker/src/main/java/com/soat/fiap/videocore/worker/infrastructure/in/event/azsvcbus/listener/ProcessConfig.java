package com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.listener;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.soat.fiap.videocore.worker.infrastructure.common.config.azure.svcbus.ServiceBusConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura o processor client para consumir eventos de processamento de vídeo do Service Bus.
 */
@Configuration
@RequiredArgsConstructor
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
                .processMessage(handler::handleMessage)
                .processError(context -> {})
                .buildProcessorClient();
    }
}
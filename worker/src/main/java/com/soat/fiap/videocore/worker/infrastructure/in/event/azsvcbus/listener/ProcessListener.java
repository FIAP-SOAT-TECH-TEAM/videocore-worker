package com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.listener;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Listener que inicia o processamento de mensagens do Service Bus após a inicialização do bean.
 */
@RequiredArgsConstructor
@Component
public class ProcessListener {

    private final ServiceBusProcessorClient processVideo;

    @PostConstruct
    public void run() {
        processVideo.start();
    }
}
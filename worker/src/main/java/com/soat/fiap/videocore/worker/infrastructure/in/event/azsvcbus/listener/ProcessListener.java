package com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.listener;

import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusProcessorClient;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * Listener que inicia o processamento de mensagens do Service Bus após a
 * inicialização do bean.
 */
@RequiredArgsConstructor @Component
public class ProcessListener {

	private final ServiceBusProcessorClient processVideo;

	@PostConstruct
	public void run() {
		processVideo.start();
	}
}

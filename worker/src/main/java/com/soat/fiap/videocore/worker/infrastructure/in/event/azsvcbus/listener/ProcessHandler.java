package com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.listener;

import org.springframework.stereotype.Component;

import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soat.fiap.videocore.worker.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.interfaceadapters.controller.ProcessVideoController;
import com.soat.fiap.videocore.worker.infrastructure.common.exceptions.azure.svcbus.ServiceBusSerializationException;
import com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.payload.BlobCreatedCloudEventSchemaPayload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler que processa mensagens de criação de blob recebidas do Service Bus.
 */
@Component @RequiredArgsConstructor @Slf4j
public class ProcessHandler {

	private final ObjectMapper objectMapper;
	private final ProcessVideoController controller;

	/**
	 * Desserializa a mensagem recebida e envia para o controller.
	 *
	 * @param context
	 *            contexto da mensagem recebida
	 */
	@WithSpan(name = "listener.process.video.event")
	public void handleMessage(ServiceBusReceivedMessageContext context) {
		try {
			var rawBody = context.getMessage().getBody().toString();
			CanonicalContext.add("event_object_string", rawBody);

			var payload = objectMapper.readValue(rawBody, BlobCreatedCloudEventSchemaPayload.class);
			CanonicalContext.add("event_object", payload);

			controller.processVideo(payload);

			log.info("request_completed");
		} catch (JsonProcessingException e) {
			log.error("request_error", e);

			throw new ServiceBusSerializationException("Erro ao desserializar evento", e);
		} catch (Exception e) {
			log.error("request_error", e);

			throw e;
		} finally {
			CanonicalContext.clear();
		}
	}
}

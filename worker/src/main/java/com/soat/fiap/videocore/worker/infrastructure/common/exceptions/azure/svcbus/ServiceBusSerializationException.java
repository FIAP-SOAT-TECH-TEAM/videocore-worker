package com.soat.fiap.videocore.worker.infrastructure.common.exceptions.azure.svcbus;

/**
 * Exceção lançada quando ocorre algum erro de serialização/desserialização no
 * Service Bus
 */
public class ServiceBusSerializationException extends RuntimeException {

	public ServiceBusSerializationException(String message) {
		super(message);
	}

	public ServiceBusSerializationException(String message, Throwable cause) {
		super(message, cause);
	}
}

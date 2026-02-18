package com.soat.fiap.videocore.worker.core.domain.exceptions;

/**
 * Exceção lançada quando ocorre um erro no processamento de um vídeo
 */
public class ProcessVideoException extends RuntimeException {

	public ProcessVideoException(String message) {
		super(message);
	}

	public ProcessVideoException(String message, Throwable cause) {
		super(message, cause);
	}
}

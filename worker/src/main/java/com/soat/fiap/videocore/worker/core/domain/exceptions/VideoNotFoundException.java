package com.soat.fiap.videocore.worker.core.domain.exceptions;

/**
 * Exceção lançada quando um vídeo não é encontrado
 */
public class VideoNotFoundException extends RuntimeException {

	public VideoNotFoundException(String message) {
		super(message);
	}

	public VideoNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public VideoNotFoundException(String message, Object... args) {
		super(String.format(message, args));
	}
}

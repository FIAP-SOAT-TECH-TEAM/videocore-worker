package com.soat.fiap.videocore.worker.core.domain.exceptions;

/**
 * Exceção lançada quando uma regra de negócio é violada referente ao video
 */
public class VideoException extends RuntimeException {

	public VideoException(String message) {
		super(message);
	}

	public VideoException(String message, Throwable cause) {
		super(message, cause);
	}
}

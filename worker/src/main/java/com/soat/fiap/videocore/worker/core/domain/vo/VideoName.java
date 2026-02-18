package com.soat.fiap.videocore.worker.core.domain.vo;

import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;

/**
 * Objeto de valor que representa o nome de um vídeo.
 */
public record VideoName(String value) {

	public VideoName {
		validate(value);
	}

	private static void validate(String value) {
		if (value == null || value.isBlank()) {
			throw new VideoException("O nome do vídeo não pode ser nulo ou vazio");
		}
	}
}

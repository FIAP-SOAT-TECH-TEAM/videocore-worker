package com.soat.fiap.videocore.worker.core.application.usecase;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoNotFoundException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.VideoGateway;

import lombok.RequiredArgsConstructor;

/**
 * Caso de uso responsável por obter um vídeo a partir da URL.
 */
@Component @RequiredArgsConstructor
public class GetVideoUseCase {

	private final VideoGateway videoGateway;

	/**
	 * Recupera um vídeo associado à URL informada.
	 *
	 * @param videoUrl
	 *            URL completa do vídeo
	 * @return o vídeo
	 * @throws VideoNotFoundException
	 *             se o vídeo não for encontrado
	 */
	@WithSpan(name = "usecase.get.video.info")
	public Video getVideo(String videoUrl) {
		if (videoUrl == null || videoUrl.isBlank())
			throw new VideoException("A URL do vídeo não pode ser nula para sua pesquisa");

		var video = videoGateway.getVideo(videoUrl);

		if (video == null)
			throw new VideoNotFoundException("Vídeo não encontrado com a URL: {}", videoUrl);

		return video;
	}
}

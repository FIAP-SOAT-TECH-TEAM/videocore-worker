package com.soat.fiap.videocore.worker.core.interfaceadapters.gateway;

import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.mapper.VideoMapper;
import com.soat.fiap.videocore.worker.infrastructure.common.source.VideoDataSource;

import lombok.RequiredArgsConstructor;

/**
 * Gateway responsável por abstrair o acesso a vídeos armazenados externamente.
 * Atua como intermediário entre a camada de aplicação e a fonte de dados.
 */
@Component @RequiredArgsConstructor
public class VideoGateway {

	private final VideoDataSource videoDataSource;
	private final VideoMapper videoMapper;

	/**
	 * Recupera o vídeo a partir de uma URL.
	 *
	 * @param videoUrl
	 *            URL completa do vídeo
	 * @return O vídeo, ou nulo se não encontrado
	 */
	@WithSpan(name = "gateway.get.video.info")
	public Video getVideo(String videoUrl) {
		var video = videoDataSource.getVideo(videoUrl);

		if (video == null)
			return null;

		return videoMapper.toModel(video);
	}

	/**
	 * Recupera um {@link ZipOutputStream} para escrita dos dados gerados a partir
	 * do processamento do vídeo.
	 *
	 * @param video
	 *            vídeo de domínio a ser processado
	 * @return {@link ZipOutputStream} associado ao vídeo
	 */
	@WithSpan(name = "gateway.get.video.zip.output.stream")
	public ZipOutputStream getZipOutputStream(Video video) {
		var videoDto = videoMapper.toDto(video);

		return videoDataSource.getZipOutputStream(videoDto);
	}
}

package com.soat.fiap.videocore.worker.core.application.usecase;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.FileGateway;

import lombok.RequiredArgsConstructor;

/**
 * Caso de uso responsável por realizar o download de um vídeo a partir de seu
 * {@link java.io.InputStream} e persistí-lo temporariamente no sistema de
 * arquivos local.
 * <p>
 * O arquivo gerado é criado no diretório temporário padrão do sistema
 * operacional e deve ser removido explicitamente pelo chamador após o uso,
 * evitando vazamento de recursos em disco.
 * <p>
 * Este caso de uso é indicado quando a API consumidora exige um {@link File} ou
 * um recurso seekable (ex.: bibliotecas que dependem de acesso aleatório ao
 * conteúdo do vídeo).
 */
@Component @RequiredArgsConstructor
public class DownloadVideoToTempFileUseCase {

	private final FileGateway fileGateway;

	/**
	 * Efetua o download do vídeo representado pelo objeto de domínio {@link Video}.
	 * Então, popula as propriedades correspondentes do objeto de domínio
	 *
	 * @param video
	 *            objeto de domínio que encapsula o stream do vídeo e seus metadados
	 * @throws ProcessVideoException
	 *             caso ocorra erro de I/O durante o download, criação do arquivo ou
	 *             captura da sua duraação
	 */
	@WithSpan(name = "usecase.download.video")
	public void downloadVideoToTempFile(Video video) {
		if (video == null)
			throw new ProcessVideoException("O video não pode ser nulo para download");

		try (var inputStream = video.getInputStream()) {
			var tempFileName = String.format("video-%s-%s-%s", video.getMetadata().requestId(),
					video.getMetadata().userId(), video.getVideoName());

			var tempFile = fileGateway.downloadTempFileFromInputStream(inputStream, video.getVideoExtension(),
					tempFileName);
			video.setVideoFile(tempFile);
		} catch (IOException e) {
			throw new ProcessVideoException("Erro ao fazer o download do vídeo", e);
		}
	}
}

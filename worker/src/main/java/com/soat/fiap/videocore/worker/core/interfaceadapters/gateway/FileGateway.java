package com.soat.fiap.videocore.worker.core.interfaceadapters.gateway;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.infrastructure.common.source.FileSource;

import lombok.RequiredArgsConstructor;

/**
 * Gateway para operações de arquivo, delegando para {@link FileSource}. Atua
 * como intermediário entre a camada de aplicação e a fonte de dados.
 */
@Component @RequiredArgsConstructor
public class FileGateway {
	private final FileSource fileSource;

	/**
	 * Remove o arquivo se ele existir.
	 *
	 * @param file
	 *            arquivo a ser deletado
	 * @throws IOException
	 *             em caso de falha na exclusão
	 */
	@WithSpan(name = "gateway.delete.video.file")
	public void deleteFileIfExists(File file) throws IOException {
		fileSource.deleteFileIfExists(file);
	}

	/**
	 * Cria um arquivo temporário a partir de um InputStream.
	 *
	 * @param inputStream
	 *            origem dos dados
	 * @param extension
	 *            extensão do arquivo
	 * @param fileName
	 *            nome base do arquivo
	 * @return arquivo temporário criado
	 * @throws IOException
	 *             em caso de erro na escrita do arquivo
	 */
	@WithSpan(name = "gateway.download.video")
	public File downloadTempFileFromInputStream(InputStream inputStream, String extension, String fileName)
			throws IOException {
		return fileSource.downloadTempFileFromInputStream(inputStream, extension, fileName);
	}
}

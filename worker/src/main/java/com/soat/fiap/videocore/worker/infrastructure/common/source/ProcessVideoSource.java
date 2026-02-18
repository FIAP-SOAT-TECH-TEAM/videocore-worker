package com.soat.fiap.videocore.worker.infrastructure.common.source;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

/**
 * Contrato para leitura de metadados e processamento de vídeos.
 */
public interface ProcessVideoSource {

	/**
	 * Retorna a duração total do vídeo em minutos.
	 *
	 * @param file
	 *            arquivo de vídeo
	 * @return duração do vídeo em minutos
	 * @throws IllegalStateException
	 *             em caso de erro na leitura do vídeo
	 */
	long getVideoDurationMinutes(File file) throws IllegalStateException;

	/**
	 * Processa o vídeo a partir de um timestamp e grava os frames em um
	 * {@link ZipOutputStream}.
	 *
	 * @param file
	 *            arquivo de vídeo
	 * @param imageName
	 *            nome da imagem a ser salva quando processada
	 * @param currentTimestamp
	 *            timestamp inicial do processamento
	 * @param zipOutputStream
	 *            destino dos frames compactados
	 * @throws IOException
	 *             em caso de erro de processamento ou escrita
	 */
	void processVideo(File file, long currentTimestamp, ZipOutputStream zipOutputStream, String imageName)
			throws IOException;
}

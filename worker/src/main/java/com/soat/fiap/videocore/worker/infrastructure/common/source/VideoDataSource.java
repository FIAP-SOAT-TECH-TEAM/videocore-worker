package com.soat.fiap.videocore.worker.infrastructure.common.source;

import java.io.OutputStream;
import java.util.zip.ZipOutputStream;

import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.VideoDto;

/**
 * Contrato para fontes de dados de vídeo.
 */
public interface VideoDataSource {

	/**
	 * Obtém um {@link VideoDto} pela URL.
	 *
	 * @param videoUrl
	 *            URL completa do vídeo
	 * @return {@link VideoDto} contendo informações do vídeo
	 */
	VideoDto getVideo(String videoUrl);

	/**
	 * Obtém um {@link ZipOutputStream} que representa um arquivo ZIP.
	 *
	 * <p>
	 * Este Stream pode ser utilizado para salvar as imagens capturadas do vídeo na
	 * fonte de dados de destino
	 * </p>
	 *
	 * @param videoDto
	 *            objeto que representa o vídeo e seus metadados
	 * @return {@link OutputStream} do ZIP gerado
	 */
	ZipOutputStream getZipOutputStream(VideoDto videoDto);

}

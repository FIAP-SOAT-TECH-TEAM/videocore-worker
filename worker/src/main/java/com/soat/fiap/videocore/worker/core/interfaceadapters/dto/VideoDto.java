package com.soat.fiap.videocore.worker.core.interfaceadapters.dto;

import java.io.File;
import java.io.InputStream;

/**
 * DTO para transporte de dados de vídeo entre camadas.
 *
 * @param videoName
 *            nome do vídeo
 * @param videoFile
 *            arquivo do vídeo
 * @param durationMinutes
 *            duração total do vídeo em minutos
 * @param inputStream
 *            stream do conteúdo bruto do vídeo
 * @param minuteFrameCut
 *            tempo de corte dos frames para captura de imaagens em minutos
 * @param userId
 *            identificador do usuário
 * @param requestId
 *            identificador da requisição
 */
public record VideoDto(String videoName, File videoFile, long durationMinutes, InputStream inputStream,
		long minuteFrameCut, String userId, String requestId) {
}

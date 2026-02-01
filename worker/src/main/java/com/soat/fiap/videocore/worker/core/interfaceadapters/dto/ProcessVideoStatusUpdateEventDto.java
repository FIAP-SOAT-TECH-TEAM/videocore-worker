package com.soat.fiap.videocore.worker.core.interfaceadapters.dto;


import java.time.Instant;
import java.util.UUID;

/**
 * DTO para transporte do evento de atualização do status de processamento do vídeo entre camadas.
 *
 * @param videoName             Nome do vídeo.
 * @param userId                Identificador do usuário dono do vídeo.
 * @param requestId             Identificador da requisição de processamento.
 * @param imageMinute           Minuto em que a imagem foi capturada.
 * @param frameCutMinutes       Intervalo de corte de frames em minutos.
 * @param percentStatusProcess  Percentual do vídeo já processado.
 * @param reportTime            Momento em que o reporte foi realizado.
 */
public record ProcessVideoStatusUpdateEventDto(
        String videoName,
        UUID userId,
        UUID requestId,
        long imageMinute,
        long frameCutMinutes,
        Double percentStatusProcess,
        Instant reportTime
) {}
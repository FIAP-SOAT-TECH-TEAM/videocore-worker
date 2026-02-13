package com.soat.fiap.videocore.worker.core.interfaceadapters.dto;

import java.time.Instant;

/**
 * DTO para transporte do evento de atualização do status de processamento do vídeo entre camadas.
 *
 * @param videoName             Nome do vídeo.
 * @param userId                Identificador do usuário dono do vídeo.
 * @param requestId             Identificador da requisição de processamento.
 * @param traceId               Identificador de rastreio (observabilidade).
 * @param imageMinute           Minuto em que a imagem foi capturada.
 * @param frameCutMinutes       Intervalo de corte de frames em minutos.
 * @param percentStatusProcess  Percentual do vídeo já processado.
 * @param reportTime            Momento em que o reporte foi realizado.
 * @param isError               Indica se o reporte se trata de um erro.
 */
public record ProcessVideoStatusUpdateEventDto(
        String videoName,
        String userId,
        String requestId,
        String traceId,
        long imageMinute,
        long frameCutMinutes,
        Double percentStatusProcess,
        Instant reportTime,
        boolean isError
) {}
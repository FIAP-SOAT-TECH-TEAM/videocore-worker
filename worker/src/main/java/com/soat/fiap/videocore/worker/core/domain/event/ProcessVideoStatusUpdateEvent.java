package com.soat.fiap.videocore.worker.core.domain.event;

import java.time.Instant;

/**
 * Evento de domínio que representa a atualização do status de processamento de um vídeo.
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
public record ProcessVideoStatusUpdateEvent(
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
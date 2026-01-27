package com.soat.fiap.videocore.worker.core.interfaceadapters.dto;

import java.util.UUID;

/**
 * DTO para transporte do evento de atualização do status de processamento do vídeo entre camadas.
 *
 * @param videoName             Nome do vídeo.
 * @param userId                Identificador do usuário dono do vídeo.
 * @param requestId             Identificador da requisição de processamento.
 * @param durationMinutes       Duração total do vídeo em minutos.
 * @param frameCut              Intervalo de corte de frames em unidades de tempo.
 * @param percentStatusProcess  Percentual do vídeo já processado.
 */
public record ProcessVideoStatusUpdateEventDto(
        String videoName,
        UUID userId,
        UUID requestId,
        long durationMinutes,
        long frameCut,
        Double percentStatusProcess
) {}
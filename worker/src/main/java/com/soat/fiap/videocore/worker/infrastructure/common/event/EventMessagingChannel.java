package com.soat.fiap.videocore.worker.infrastructure.common.event;

/**
 * Centraliza os nomes dos canais de mensageria (filas, tópicos e subscriptions).
 */
public final class EventMessagingChannel {

    private EventMessagingChannel() {
    }

    /**
     * Fila principal de processamento de eventos.
     * <p>Responsável pelo fluxo normal de processamento.</p>
     */
    public static final String PROCESS_QUEUE = "process.queue";

    /**
     * Tópico de status do processamento.
     * <p>Publica atualizações de estado dos processos.</p>
     */
    public static final String PROCESS_STATUS_TOPIC = "process.status.topic";
}
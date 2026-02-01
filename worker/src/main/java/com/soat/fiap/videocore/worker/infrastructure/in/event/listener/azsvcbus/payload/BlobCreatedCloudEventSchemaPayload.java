package com.soat.fiap.videocore.worker.infrastructure.in.event.listener.azsvcbus.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

/**
 * Payload que representa um evento do tipo Microsoft.Storage.BlobCreated,
 * conforme o esquema CloudEvent do Azure Event Grid para Blob Storage.
 * <a href="https://learn.microsoft.com/en-us/azure/event-grid/event-schema-blob-storage?tabs=cloud-event-schema#example-events">Microsoft.Storage.BlobCreated event</a>
 * </p>
 */
@Data
@NoArgsConstructor
public class BlobCreatedCloudEventSchemaPayload {

    /**
     * Identifica o recurso de origem do evento.
     */
    private String source;

    /**
     * Caminho específico do recurso afetado pelo evento.
     */
    private String subject;

    /**
     * Tipo do evento emitido.
     */
    private String type;

    /**
     * Data e hora em que o evento ocorreu (UTC).
     */
    private OffsetDateTime time;

    /**
     * Identificador único do evento.
     */
    private String id;

    /**
     * Payload contendo os detalhes específicos do evento.
     */
    private DataPayload data;

    /**
     * Versão da especificação CloudEvents utilizada.
     */
    private String specversion;

    /**
     * Representa o objeto "data" do evento,
     * conforme o esquema de Blob Storage.
     */
    @Data
    @NoArgsConstructor
    public static class DataPayload {

        private String api;
        private String clientRequestId;
        private String requestId;
        private String eTag;
        private String contentType;
        private long contentLength;
        private String blobType;
        private String accessTier;
        private String url;
        private String sequencer;
        private StorageDiagnostics storageDiagnostics;

        @Data
        @NoArgsConstructor
        public static class StorageDiagnostics {
            private String batchId;
        }
    }
}
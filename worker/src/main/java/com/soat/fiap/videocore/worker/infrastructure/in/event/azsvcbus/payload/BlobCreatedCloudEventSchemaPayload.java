package com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.payload;

import java.time.OffsetDateTime;

/**
 * Payload que representa um evento do tipo Microsoft.Storage.BlobCreated,
 * conforme o esquema CloudEvent do Azure Event Grid para Blob Storage. <a href=
 * "https://learn.microsoft.com/en-us/azure/event-grid/event-schema-blob-storage?tabs=cloud-event-schema#example-events">Microsoft.Storage.BlobCreated
 * event</a>
 * </p>
 */
public record BlobCreatedCloudEventSchemaPayload(String source, String subject, String type, OffsetDateTime time,
		String id, DataPayload data, String specversion) {
	/**
	 * Representa o objeto "data" do evento, conforme o esquema de Blob Storage.
	 */
	public record DataPayload(String api, String clientRequestId, String requestId, String eTag, String contentType,
			long contentLength, String blobType, String accessTier, String url, String sequencer,
			StorageDiagnostics storageDiagnostics) {
		public record StorageDiagnostics(String batchId) {
		}
	}
}

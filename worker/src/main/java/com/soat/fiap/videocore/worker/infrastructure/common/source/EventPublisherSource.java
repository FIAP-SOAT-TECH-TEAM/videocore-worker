package com.soat.fiap.videocore.worker.infrastructure.common.source;

import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.ProcessVideoStatusUpdateEventDto;

/**
 * Interface para publicação de eventos.
 */
public interface EventPublisherSource {

	/**
	 * Publica um evento indicando a atualização do status de processamento de
	 * vídeo.
	 *
	 * @param event
	 *            DTO contendo informações do evento de atualização
	 */
	void publishVideoStatusProcessUpdateEvent(ProcessVideoStatusUpdateEventDto event);
}

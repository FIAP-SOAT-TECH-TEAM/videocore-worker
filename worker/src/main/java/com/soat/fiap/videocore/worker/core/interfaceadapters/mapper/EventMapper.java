package com.soat.fiap.videocore.worker.core.interfaceadapters.mapper;

import org.mapstruct.Mapper;

import com.soat.fiap.videocore.worker.core.domain.event.ProcessVideoStatusUpdateEvent;
import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.ProcessVideoStatusUpdateEventDto;

/**
 * Mapper genérico para conversão entre objetos de domínio e DTOs de eventos.
 */
@Mapper(componentModel = "spring")
public interface EventMapper {

	/**
	 * Converte um evento de domínio em um DTO de transporte.
	 *
	 * @param event
	 *            Evento de domínio a ser convertido.
	 * @return DTO correspondente ao evento.
	 */
	ProcessVideoStatusUpdateEventDto toDto(ProcessVideoStatusUpdateEvent event);

	/**
	 * Converte um DTO de transporte em um evento de domínio.
	 *
	 * @param dto
	 *            DTO a ser convertido.
	 * @return Evento de domínio correspondente ao DTO.
	 */
	ProcessVideoStatusUpdateEvent toDomain(ProcessVideoStatusUpdateEventDto dto);
}

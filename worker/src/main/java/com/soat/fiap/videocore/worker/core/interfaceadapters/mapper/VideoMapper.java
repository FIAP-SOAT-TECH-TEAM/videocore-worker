package com.soat.fiap.videocore.worker.core.interfaceadapters.mapper;

import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.VideoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper MapStruct responsável pelo mapeamento entre os objetos
 * {@link Video} (modelo de domínio) e {@link VideoDto} (DTO).
 */
@Mapper(componentModel = "spring")
public interface VideoMapper {

    /**
     * Converte um {@link VideoDto} em {@link Video}.
     *
     * @param dto DTO contendo os dados do vídeo
     * @return instância de {@link Video}
     */
    @Mapping(
            target = "videoName",
            expression = "java(new VideoName(dto.videoName()))"
    )
    @Mapping(
            target = "videoFile",
            ignore = true
    )
    @Mapping(
            target = "durationMinutes",
            ignore = true
    )
    @Mapping(
            target = "minuteFrameCut",
            expression = "java(new MinuteFrameCut(dto.minuteFrameCut()))"
    )
    @Mapping(
            target = "metadata",
            expression = "java(new Metadata(dto.userId(), dto.requestId()))"
    )
    Video toModel(VideoDto dto);

    /**
     * Converte um {@link Video} em {@link VideoDto}.
     *
     *
     * @param video modelo de domínio do vídeo
     * @return DTO {@link VideoDto} equivalente
     */
    @Mapping(
            target = "videoName",
            expression = "java(video.getVideoName())"
    )
    @Mapping(
            target = "videoFile",
            expression = "java(video.getVideoFile())"
    )
    @Mapping(
            target = "durationMinutes",
            expression = "java(video.getDurationMinutes())"
    )
    @Mapping(
            target = "minuteFrameCut",
            expression = "java(video.getMinuteFrameCut())"
    )
    @Mapping(
            target = "userId",
            expression = "java(video.getUserId())"
    )
    @Mapping(
            target = "requestId",
            expression = "java(video.getRequestId())"
    )
    VideoDto toDto(Video video);
}
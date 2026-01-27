package com.soat.fiap.videocore.worker.infrastructure.out.blobstorage.mapper;

import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.VideoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

/**
 * Mapper MapStruct responsável pelo mapeamento entre objetos Blob e VideoDTO.
 */
@Mapper(componentModel = "spring", imports = {UUID.class, Integer.class})
public interface VideoBlobMapper {

    /**
     * Converte os dados brutos de um blob em um {@link VideoDto}.
     *
     * @param inputStream stream do conteúdo do blob
     * @param metadata mapa de metadados associados ao blob
     * @param blobName nome completo do blob no container
     * @return instância de {@link VideoDto} com os dados mapeados
     */
    @Mapping(
            target = "videoName",
            expression = "java(extractVideoName(blobName))"
    )
    @Mapping(
            target = "durationMinutes",
            ignore = true
    )
    @Mapping(
            target = "videoFile",
            ignore = true
    )
    @Mapping(target = "inputStream", source = "inputStream")
    @Mapping(
            target = "minuteFrameCut",
            expression = "java(Long.parseLong(metadata.get(\"frame_cut\")))"
    )
    @Mapping(
            target = "userId",
            expression = "java(UUID.fromString(metadata.get(\"user_id\")))"
    )
    @Mapping(
            target = "requestId",
            expression = "java(UUID.fromString(metadata.get(\"request_id\")))"
    )
    VideoDto toDto(InputStream inputStream, Map<String, String> metadata, String blobName);

    default String extractVideoName(String blobName) {
        if (blobName == null) {
            return null;
        }
        var lastSlash = blobName.lastIndexOf('/');
        var fileName = lastSlash >= 0 ? blobName.substring(lastSlash + 1) : blobName;
        var lastDot = fileName.lastIndexOf('.');
        return lastDot > 0 ? fileName.substring(0, lastDot) : fileName;
    }
}

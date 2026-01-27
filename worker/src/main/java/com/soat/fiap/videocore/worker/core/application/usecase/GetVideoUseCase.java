package com.soat.fiap.videocore.worker.core.application.usecase;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoNotFoundException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.VideoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por obter um vídeo
 * a partir da URL.
 */
@Component
@RequiredArgsConstructor
public class GetVideoUseCase {

    private final VideoGateway videoGateway;

    /**
     * Recupera um vídeo associado à URL informada.
     *
     * @param videoUrl URL completa do vídeo
     * @return o vídeo
     * @throws VideoNotFoundException se o vídeo não for encontrado
     */
    @WithSpan(name = "process.video.get.info")
    public Video getVideo(String videoUrl) {
        var video = videoGateway.getVideo(videoUrl);

        if (video == null)
            throw new VideoNotFoundException("Vídeo não encontrado com a URL: {}", videoUrl);

        return video;
    }
}
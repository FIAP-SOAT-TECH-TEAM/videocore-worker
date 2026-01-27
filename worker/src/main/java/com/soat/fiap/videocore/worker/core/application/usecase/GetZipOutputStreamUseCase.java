package com.soat.fiap.videocore.worker.core.application.usecase;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.VideoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.zip.ZipOutputStream;

/**
 * Caso de uso responsável por obter o {@link ZipOutputStream}
 * associado a um {@link Video}.
 */
@Component
@RequiredArgsConstructor
public class GetZipOutputStreamUseCase {

    private final VideoGateway videoGateway;

    /**
     * Retorna o {@link ZipOutputStream} para escrita do processamento do vídeo.
     *
     * @param video vídeo de origem
     * @return stream ZIP válido
     * @throws ProcessVideoException se o stream não puder ser obtido
     */
    @WithSpan(name = "process.video.get.zip-output-stream")
    public ZipOutputStream getZipOutputStream(Video video) {
        var zipOutputStream = videoGateway.getZipOutputStream(video);

        if (zipOutputStream == null) {
            throw new ProcessVideoException("ZIP Stream não obtida");
        }

        return zipOutputStream;
    }
}
package com.soat.fiap.videocore.worker.core.application.usecase;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.domain.vo.DurationMinutes;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.ProcessVideoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Caso de uso responsável por obter a duração de um vídeo em minutos
 * e atualizar o objeto {@link Video} com essa informação.
 */
@Component
@RequiredArgsConstructor
public class GetVideoDurationMinutesUseCase {
    private final ProcessVideoGateway processVideoGateway;

    /**
     * Recupera a duração do vídeo em minutos usando o {@link ProcessVideoGateway}
     * e seta no objeto {@link Video}.
     *
     * @param video objeto de vídeo a ser atualizado com a duração
     * @throws ProcessVideoException caso ocorra algum erro ao obter a duração
     */
    @WithSpan(name = "usecase.get.video.duration.minutes")
    public void getVideoDurationMinutes(Video video) {
        if (video == null)
            throw new ProcessVideoException("O video não pode ser nulo para obtenção da sua duração");

        try {
            var durationFileInMinutes = processVideoGateway.getVideoDurationMinutes(video.getVideoFile());
            var durationMinutes = new DurationMinutes(durationFileInMinutes);

            video.setDurationMinutes(durationMinutes);
        }
        catch (IllegalStateException e) {
            throw new ProcessVideoException("Erro ao obter duração do vídeo", e);
        }
    }
}
package com.soat.fiap.videocore.worker.core.application.usecase;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.event.ProcessVideoStatusUpdateEvent;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.EventPublisherGateway;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.ProcessVideoGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.zip.ZipOutputStream;

/**
 * Caso de uso responsável por processar um vídeo e extrair frames
 * em intervalos fixos de tempo, exportando-os como imagens em um ZIP.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProcessVideoUseCase {

    private final ProcessVideoGateway processVideoGateway;
    private final EventPublisherGateway eventPublisherGateway;

    /**
     * Processa o vídeo, capturando frames em intervalos definidos em minutos
     * e escrevendo-os sequencialmente em um {@link ZipOutputStream}.
     *
     * @param video objeto de domínio do vídeo a ser processado
     * @param zipOutputStream destino onde as imagens serão compactadas
     * @throws ProcessVideoException em caso de erro durante o processamento do vídeo
     */
    @WithSpan(name = "process.video.image")
    public void processVideo(Video video, ZipOutputStream zipOutputStream) {
        var frameCutMicro = video.getMinuteFrameCut() * 60_000_000L;
        var totalDurationMicro = video.getDurationMinutes() * 60_000_000L;

        try (zipOutputStream) {
            for (var currentTimestampMicro = 0L; currentTimestampMicro <= totalDurationMicro; currentTimestampMicro += frameCutMicro) {
                var cutMomentMinutes = currentTimestampMicro / 60_000_000L;
                var imageName = String.format("frame_at_%dm.jpg", cutMomentMinutes);

                processVideoGateway.processVideo(video.getVideoFile(), currentTimestampMicro, zipOutputStream, imageName);

                var currentPercent = ((double) currentTimestampMicro / totalDurationMicro) * 100;

                log.debug(String.format("Vídeo: %s processado %.2f%%", video.getVideoName(), currentPercent));

                var updateStatusEvent = new ProcessVideoStatusUpdateEvent(
                        video.getVideoName(),
                        video.getUserId(),
                        video.getRequestId(),
                        video.getDurationMinutes(),
                        video.getMinuteFrameCut(),
                        currentPercent
                );
                eventPublisherGateway.publishVideoStatusProcessUpdateEvent(updateStatusEvent);

                var nextCurrentTimestampMicro = currentTimestampMicro + frameCutMicro;
                if (currentTimestampMicro < totalDurationMicro && nextCurrentTimestampMicro > totalDurationMicro)
                    frameCutMicro = totalDurationMicro - currentTimestampMicro;
            }

        } catch (Exception e) {
            throw new ProcessVideoException(String.format("Erro ao processar vídeo: %s", e.getMessage()), e);
        }
    }
}
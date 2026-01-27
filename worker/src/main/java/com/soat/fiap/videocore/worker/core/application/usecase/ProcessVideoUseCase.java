package com.soat.fiap.videocore.worker.core.application.usecase;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.ProcessVideoGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.zip.ZipOutputStream;

/**
 * Caso de uso responsável por processar um vídeo e extrair frames
 * em intervalos fixos de tempo, exportando-os como imagens em um ZIP.
 */
@Component
@RequiredArgsConstructor
public class ProcessVideoUseCase {

    private final ProcessVideoGateway processVideoGateway;

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
        var frameCutMicro = Math.round(video.getMinuteFrameCut() * 60_000_000L);
        var totalDurationMicro = Math.round(video.getDurationMinutes() * 60_000_000L);

        try (zipOutputStream) {
            for (var currentTimestamp = 0L; currentTimestamp < totalDurationMicro; currentTimestamp += frameCutMicro) {
                var cutMomentMinutes = currentTimestamp / 60_000_000L;
                var imageName = String.format("frame_at_%dm.jpg", cutMomentMinutes);

                processVideoGateway.processVideo(video.getVideoFile(), currentTimestamp, zipOutputStream, imageName);
            }

        } catch (Exception e) {
            throw new ProcessVideoException(String.format("Erro ao processar vídeo: %s", e.getMessage()), e);
        }
    }
}
package com.soat.fiap.videocore.worker.core.application.usecase;

import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.application.input.VideoProcessMetadataInput;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.ProcessVideoGateway;

import lombok.RequiredArgsConstructor;

/**
 * Caso de uso responsável por processar um vídeo e extrair frames em intervalos
 * fixos de tempo, exportando-os como imagens em um ZIP.
 */
@Component @RequiredArgsConstructor
public class ProcessVideoUseCase {

	private final PublishVideoStatusProcessUpdateUseCase publishVideoStatusProcessUpdateUseCase;
	private final ProcessVideoGateway processVideoGateway;

	/**
	 * Processa o vídeo, capturando frames em intervalos definidos em minutos e
	 * escrevendo-os sequencialmente em um {@link ZipOutputStream}.
	 *
	 * @param video
	 *            objeto de domínio do vídeo a ser processado
	 * @param zipOutputStream
	 *            destino onde as imagens serão compactadas
	 * @param videoProcessMetadata
	 *            dados de acompanhamento do processamento de vídeo.
	 * @throws ProcessVideoException
	 *             em caso de erro durante o processamento do vídeo
	 */
	@WithSpan(name = "usecase.process.video")
	public void processVideo(Video video, ZipOutputStream zipOutputStream,
			VideoProcessMetadataInput videoProcessMetadata) {
		if (video == null || zipOutputStream == null)
			throw new ProcessVideoException("O video ou o output stream não podem ser nulos para o processamento");

		var frameCutMicro = video.getMinuteFrameCut() * 60_000_000L;
		var totalDurationMicro = video.getDurationMinutes() * 60_000_000L;

		try (zipOutputStream) {
			for (var currentTimestampMicro = 0L; currentTimestampMicro <= totalDurationMicro; currentTimestampMicro += frameCutMicro) {
				videoProcessMetadata.setImageMinute(currentTimestampMicro / 60_000_000L);
				var imageName = String.format("frame_at_%dm.jpg", videoProcessMetadata.getImageMinute());

				processVideoGateway.processVideo(video.getVideoFile(), currentTimestampMicro, zipOutputStream,
						imageName);

				videoProcessMetadata.setCurrentPercent(((double) currentTimestampMicro / totalDurationMicro) * 100);
				publishVideoStatusProcessUpdateUseCase.publishVideoStatusProcessUpdate(video,
						videoProcessMetadata.getCurrentPercent(), videoProcessMetadata.getImageMinute(), false);

				var nextCurrentTimestampMicro = currentTimestampMicro + frameCutMicro;
				if (currentTimestampMicro < totalDurationMicro && nextCurrentTimestampMicro > totalDurationMicro)
					frameCutMicro = totalDurationMicro - currentTimestampMicro;
			}

		} catch (Exception e) {
			throw new ProcessVideoException(String.format("Erro ao processar vídeo: %s", e.getMessage()), e);
		}
	}
}

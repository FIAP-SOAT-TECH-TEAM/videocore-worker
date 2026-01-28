package com.soat.fiap.videocore.worker.core.domain.model;

import com.soat.fiap.videocore.worker.core.domain.exceptions.VideoException;
import com.soat.fiap.videocore.worker.core.domain.vo.DurationMinutes;
import com.soat.fiap.videocore.worker.core.domain.vo.MinuteFrameCut;
import com.soat.fiap.videocore.worker.core.domain.vo.Metadata;
import com.soat.fiap.videocore.worker.core.domain.vo.VideoName;
import lombok.Getter;

import java.io.File;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;

/**
 * Objeto de domínio que representa um vídeo carregado para processamento.
 */
@Getter
public class Video {

    /**
     * Nome do vídeo.
     */
    private final VideoName videoName;

    /**
     * Arquivo do vídeo.
     */
    private File videoFile;

    /**
     * Duração total do vídeo em minutos.
     */
    private DurationMinutes durationMinutes;

    /**
     * Stream do conteúdo do vídeo para leitura e processamento.
     */
    private final InputStream inputStream;

    /**
     * Tempo de corte dos frames para captura de imagens.
     */
    private final MinuteFrameCut minuteFrameCut;

    /**
     * Metadados de rastreabilidade do processamento.
     */
    private final Metadata metadata;

    public Video(VideoName videoName, File videoFile, DurationMinutes durationMinutes, InputStream inputStream, MinuteFrameCut minuteFrameCut, Metadata metadata) {
        this.videoName = videoName;
        this.videoFile = videoFile;
        this.inputStream = inputStream;
        this.durationMinutes = durationMinutes;
        this.minuteFrameCut = minuteFrameCut;
        this.metadata = metadata;
        validate();
    }

    private void validate() {
        Objects.requireNonNull(videoName, "videoName não pode ser nulo");
        Objects.requireNonNull(inputStream, "inputStream não pode ser nulo");
        Objects.requireNonNull(minuteFrameCut, "minuteFrameCut não pode ser nulo");
        Objects.requireNonNull(metadata, "metadata não pode ser nulo");
    }

    /**
     * Define o arquivo de vídeo a ser processado.
     *
     * @param videoFile arquivo do vídeo
     * @throws IllegalArgumentException se o arquivo for nulo
     */
    public void setVideoFile(File videoFile) {
        if (videoFile == null)
            throw new VideoException("videoFile não pode ser nulo");


        this.videoFile = videoFile;
    }


    /**
     * Define a duração total do vídeo em minutos.
     *
     * <p>Antes de atribuir o valor, valida se o tempo configurado para corte
     * de frames ({@link MinuteFrameCut}) é menor que a duração total do vídeo.
     * Essa validação garante que o processo de captura de imagens não tente
     * acessar instantes inexistentes no vídeo.</p>
     *
     * @param durationMinutes objeto de valor que representa a duração total do vídeo em minutos
     * @throws VideoException caso o tempo de corte para captura de imagens
     *                        seja maior ou igual à duração total do vídeo
     */
    public void setDurationMinutes(DurationMinutes durationMinutes) {
        if (durationMinutes == null)
            throw new VideoException("durationMinutes não pode ser nulo");

        if (this.minuteFrameCut.value() >= durationMinutes.value())
            throw new VideoException("O tempo de corte para captura de imagens deve ser menor que a duração total do vídeo");

       this.durationMinutes = durationMinutes;
    }

    /**
     * Retorna a extensão do vídeo com base no nome do arquivo.
     * <p>
     * Caso o nome não possua extensão válida, retorna ".mp4" como padrão.
     *
     * @return extensão do vídeo incluindo o ponto (ex.: ".mp4", ".mov")
     */
    public String getVideoExtension() {
        var name = videoName.value();
        var lastDotIndex = name.lastIndexOf('.');

        if (lastDotIndex == -1 || lastDotIndex == name.length() - 1) {
            return ".mp4";
        }

        return name.substring(lastDotIndex);
    }

    /**
     * Retorna o nome do vídeo como valor primitivo.
     *
     * @return nome do vídeo
     */
    public String getVideoName() {
        return videoName.value();
    }

    /**
     * Retorna a duração total do vídeo em minutos.
     *
     * @return duração em minutos
     */
    public long getDurationMinutes() {
        return durationMinutes.value();
    }

    /**
     * Retorna o intervalo de captura de frames em minutos.
     *
     * @return intervalo de captura em minutos
     */
    public long getMinuteFrameCut() {
        return minuteFrameCut.value();
    }

    /**
     * Retorna o identificador do usuário associado ao processamento.
     *
     * @return userId
     */
    public UUID getUserId() {
        return metadata.userId();
    }

    /**
     * Retorna o identificador da requisição associada ao processamento.
     *
     * @return requestId
     */
    public UUID getRequestId() {
        return metadata.requestId();
    }
}
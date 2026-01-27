package com.soat.fiap.videocore.worker.core.interfaceadapters.gateway;

import com.soat.fiap.videocore.worker.infrastructure.common.source.ProcessVideoSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

/**
 * Gateway responsável por delegar o processamento de vídeo à camada de infraestrutura.
 * Atua como intermediário entre a camada de aplicação e a fonte de dados.
 */
@Component
@RequiredArgsConstructor
public class ProcessVideoGateway {

    private final ProcessVideoSource processVideoSource;

    /**
     * Obtém a duração total do vídeo em minutos.
     *
     * @param file arquivo de vídeo
     * @return duração do vídeo em minutos
     * @throws IllegalStateException em caso de erro na leitura do vídeo
     */
    public long getVideoDurationMinutes(File file) throws IllegalStateException {
        return processVideoSource.getVideoDurationMinutes(file);
    }

    /**
     * Processa o vídeo a partir de um timestamp e grava os frames em um {@link ZipOutputStream}.
     *
     * @param file arquivo de vídeo
     * @param imageName nome da imagem a ser salva quando processada
     * @param currentTimestamp timestamp inicial do processamento
     * @param zipOutputStream destino dos frames compactados
     * @throws IOException em caso de erro de escrita
     */
    public void processVideo(File file, long currentTimestamp, ZipOutputStream zipOutputStream, String imageName) throws IOException {
        processVideoSource.processVideo(file, currentTimestamp, zipOutputStream, imageName);
    }
}
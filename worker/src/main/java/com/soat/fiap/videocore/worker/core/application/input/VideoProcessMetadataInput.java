package com.soat.fiap.videocore.worker.core.application.input;

import lombok.Data;

/**
 * Input utilizado para representar dados de acompanhamento do processamento de vídeo.
 */
@Data
public class VideoProcessMetadataInput {

    /**
     * Percentual atual de conclusão do processamento do vídeo.
     * Valor esperado entre 0.0 e 100.0.
     */
    private double currentPercent = 0.0;

    /**
     * Minuto do vídeo referente à imagem/frame atualmente processado.
     */
    private long imageMinute = 0;

}
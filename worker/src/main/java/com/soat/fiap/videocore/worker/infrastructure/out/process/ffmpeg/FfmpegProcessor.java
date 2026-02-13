package com.soat.fiap.videocore.worker.infrastructure.out.process.ffmpeg;

import com.soat.fiap.videocore.worker.infrastructure.common.source.ProcessVideoSource;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Implementação de processamento de vídeo utilizando FFmpeg.
 */
@Component
public class FfmpegProcessor implements ProcessVideoSource {

    /**
     * Calcula a duração total do vídeo em minutos a partir dos metadados.
     *
     * @param file arquivo de vídeo
     * @return duração do vídeo em minutos
     * @throws IllegalStateException em caso de falha na inicialização do FFmpeg
     */
    @Override
    public long getVideoDurationMinutes(File file) throws IllegalStateException {
        try (var grabber = new FFmpegFrameGrabber(file)) {
            grabber.start(true);
            return grabber.getLengthInTime() / 60_000_000L;
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Captura um frame do vídeo no timestamp informado e grava a imagem no ZIP.
     *
     * @param file arquivo de vídeo
     * @param imageName nome da imagem a ser salva quando processada
     * @param currentTimestamp timestamp do frame em microssegundos
     * @param zipOutputStream destino da imagem compactada
     * @throws IOException em caso de erro de leitura ou escrita
     */
    @Override
    public void processVideo(File file, long currentTimestamp, ZipOutputStream zipOutputStream, String imageName) throws IOException {
        try (var grabber = new FFmpegFrameGrabber(file)) {
            grabber.start(false);

            var converter = new Java2DFrameConverter();
            grabber.setTimestamp(currentTimestamp);

            var frame = grabber.grabImage();
            var bufferedImage = converter.convert(frame);

            if (bufferedImage != null) {
                var zipEntry = new ZipEntry(imageName);

                var startOfExtension = imageName.lastIndexOf(".") + 1;
                var extension = imageName.substring(startOfExtension);

                zipOutputStream.putNextEntry(zipEntry);
                ImageIO.write(bufferedImage, extension, zipOutputStream);
                zipOutputStream.closeEntry();

                bufferedImage.flush();
            }
        }
    }
}
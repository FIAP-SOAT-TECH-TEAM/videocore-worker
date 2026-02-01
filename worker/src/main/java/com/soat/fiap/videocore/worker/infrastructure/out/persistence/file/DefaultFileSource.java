package com.soat.fiap.videocore.worker.infrastructure.out.persistence.file;

import com.soat.fiap.videocore.worker.infrastructure.common.source.FileSource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Implementação padrão de {@link FileSource} usando o sistema de arquivos local.
 */
@Component
public class DefaultFileSource implements FileSource {

    /**
     * Remove o arquivo se ele existir no sistema de arquivos.
     *
     * @param file arquivo a ser deletado
     * @throws IOException em caso de falha na exclusão
     */
    @Override
    public void deleteFileIfExists(File file) throws IOException {
        Files.deleteIfExists(file.toPath());
    }

    /**
     * Cria um arquivo temporário a partir de um InputStream.
     *
     * @param inputStream origem dos dados
     * @param extension extensão do arquivo
     * @param fileName nome base do arquivo
     * @return arquivo temporário criado
     * @throws IOException em caso de erro na escrita do arquivo
     */
    @Override
    public File downloadTempFileFromInputStream(InputStream inputStream, String extension, String fileName) throws IOException {
        var tempFile = Files.createTempFile(fileName, extension);
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

        return tempFile.toFile();
    }
}
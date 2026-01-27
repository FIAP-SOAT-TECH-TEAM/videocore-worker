package com.soat.fiap.videocore.worker.infrastructure.common.source;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Interface para operações básicas de manipulação de arquivos.
 */
public interface FileSource {

    /**
     * Remove o arquivo se ele existir.
     *
     * @param file arquivo a ser deletado
     * @throws IOException em caso de falha na exclusão
     */
    void deleteFileIfExists(File file) throws IOException;

    /**
     * Cria um arquivo temporário a partir de um InputStream.
     *
     * @param inputStream origem dos dados
     * @param extension extensão do arquivo
     * @param fileName nome base do arquivo
     * @return arquivo temporário criado
     * @throws IOException em caso de erro na escrita do arquivo
     */
    File downloadTempFileFromInputStream(InputStream inputStream, String extension, String fileName) throws IOException;
}
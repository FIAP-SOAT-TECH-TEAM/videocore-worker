package com.soat.fiap.videocore.worker.core.application.usecase;

import com.soat.fiap.videocore.worker.common.observability.trace.WithSpan;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.FileGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * Caso de uso responsável pela remoção de arquivos de vídeo armazenados
 * temporariamente no sistema de arquivos local.
 * <p>
 * Este caso de uso deve ser executado após o término do processamento do vídeo,
 * garantindo a liberação de espaço em disco e evitando acúmulo de arquivos temporários.
 */
@Component
@RequiredArgsConstructor
public class DeleteVideoFileUseCase {

    private final FileGateway fileGateway;

    /**
     * Exclui um arquivo de vídeo do sistema de arquivos, caso ele exista.
     *
     * @param video objeto de domínio contendo o arquivo do vídeo a ser removido
     * @throws ProcessVideoException caso ocorra erro de I/O durante a exclusão do arquivo
     */
    @WithSpan(name = "process.video.delete")
    public void deleteVideoFile(Video video) {
        try {
            if (video != null && video.getVideoFile() != null)
                fileGateway.deleteFileIfExists(video.getVideoFile());

        } catch (Exception e) {
            throw new ProcessVideoException(
                    "Erro ao excluir arquivo temporário do vídeo processado",
                    e
            );
        }
    }
}
package com.soat.fiap.videocore.worker.infrastructure.common.azure.blobstorage;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Properties do Azure Blob Storage.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "azure.blob-storage")
public class AzureBlobStorageProperties {

    /**
     * String de conexão completa com a conta do Azure Blob Storage. Essa string
     * pode ser copiada diretamente do portal do Azure.
     */
    private String connectionString;

    /**
     * Nome do container de blobs onde os videos estão armazenados.
     */
    private String videoContainerName;

    /**
     * Nome do container de blobs onde as imagens serão armazenadas.
     */
    private String imageContainerName;
}

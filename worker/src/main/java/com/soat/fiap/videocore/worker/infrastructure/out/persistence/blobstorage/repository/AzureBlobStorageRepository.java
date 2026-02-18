package com.soat.fiap.videocore.worker.infrastructure.out.persistence.blobstorage.repository;

import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

import com.azure.storage.blob.BlobClientBuilder;
import com.soat.fiap.videocore.worker.common.observability.log.CanonicalContext;
import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.VideoDto;
import com.soat.fiap.videocore.worker.infrastructure.common.config.azure.blobstorage.AzureBlobStorageProperties;
import com.soat.fiap.videocore.worker.infrastructure.common.source.VideoDataSource;
import com.soat.fiap.videocore.worker.infrastructure.out.persistence.blobstorage.mapper.VideoBlobMapper;

import lombok.RequiredArgsConstructor;

/**
 * Repositório de acesso a vídeos no Azure Blob Storage.
 */
@Component @RequiredArgsConstructor
public class AzureBlobStorageRepository implements VideoDataSource {

	private final AzureBlobStorageProperties properties;
	private final VideoBlobMapper videoBlobMapper;

	/**
	 * Recupera o vídeo e seus metadados a partir da URL do blob.
	 */
	@Override
	public VideoDto getVideo(String videoUrl) {

		var blobClient = new BlobClientBuilder().endpoint(videoUrl)
				.connectionString(properties.getConnectionString())
				.buildClient();

		if (!blobClient.exists()) {
			return null;
		}

		var blobName = blobClient.getBlobName();
		var properties = blobClient.getProperties();
		var metadata = properties.getMetadata();

		var inputStream = blobClient.openInputStream();

		return videoBlobMapper.toDto(inputStream, metadata, blobName);
	}

	/**
	 * Cria um ZipOutputStream a partir de um BlobOutputStream, construido com as
	 * informações de um vídeo
	 */
	@Override
	public ZipOutputStream getZipOutputStream(VideoDto videoDto) {
		var blobName = String.format("%s/%s/%s.zip", videoDto.userId(), videoDto.requestId(), videoDto.videoName());

		var blobClient = new BlobClientBuilder().connectionString(properties.getConnectionString())
				.containerName(properties.getImageContainerName())
				.blobName(blobName)
				.buildClient();

		CanonicalContext.add("processed_image_url", blobClient.getBlobUrl());

		var blockBlobClient = blobClient.getBlockBlobClient();

		if (blockBlobClient.exists())
			blockBlobClient.delete();

		var blobOutputStream = blockBlobClient.getBlobOutputStream();

		return new ZipOutputStream(blobOutputStream);
	}
}

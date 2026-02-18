package com.soat.fiap.videocore.worker.fixture;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.domain.vo.DurationMinutes;
import com.soat.fiap.videocore.worker.core.domain.vo.Metadata;
import com.soat.fiap.videocore.worker.core.domain.vo.MinuteFrameCut;
import com.soat.fiap.videocore.worker.core.domain.vo.VideoName;

/**
 * Fixture para criação de objetos {@link Video} válidos para testes unitários.
 */
public final class VideoFixture {

	private VideoFixture() {
	}

	/**
	 * Cria uma instância válida de {@link Video}.
	 *
	 * @return vídeo válido
	 */
	public static Video validVideo() {
		return new Video(new VideoName("video.mp4"), new File("video.mp4"), new DurationMinutes(10), inputStream(),
				new MinuteFrameCut(1), new Metadata("user-1", "request-1"));
	}

	/**
	 * Cria um {@link InputStream} simples para testes.
	 *
	 * @return input stream
	 */
	public static InputStream inputStream() {
		return new ByteArrayInputStream(new byte[]{1, 2, 3});
	}
}

package com.soat.fiap.videocore.worker.unit.usecase;

import com.soat.fiap.videocore.worker.core.application.usecase.PublishVideoStatusProcessUpdateUseCase;
import com.soat.fiap.videocore.worker.core.domain.exceptions.ProcessVideoException;
import com.soat.fiap.videocore.worker.core.domain.model.Video;
import com.soat.fiap.videocore.worker.core.interfaceadapters.gateway.EventPublisherGateway;
import com.soat.fiap.videocore.worker.fixture.VideoFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do {@link PublishVideoStatusProcessUpdateUseCase}.
 */
class PublishVideoStatusProcessUpdateUseCaseTest {

    @Test
    void shouldPublishEvent() {
        // arrange
        EventPublisherGateway gateway = mock(EventPublisherGateway.class);
        PublishVideoStatusProcessUpdateUseCase useCase =
                new PublishVideoStatusProcessUpdateUseCase(gateway);
        Video video = VideoFixture.validVideo();

        // act
        useCase.publishVideoStatusProcessUpdate(video, 10.0, 1L, false);

        // assert
        verify(gateway).publishVideoStatusProcessUpdateEvent(any());
    }

    @Test
    void shouldThrowExceptionWhenVideoIsNull() {
        // arrange
        PublishVideoStatusProcessUpdateUseCase useCase =
                new PublishVideoStatusProcessUpdateUseCase(mock(EventPublisherGateway.class));

        // act / assert
        assertThrows(ProcessVideoException.class,
                () -> useCase.publishVideoStatusProcessUpdate(null, 10.0, 1L, false));
    }
}
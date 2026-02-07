package com.soat.fiap.videocore.worker.unit.vo;

import com.soat.fiap.videocore.worker.core.domain.vo.Metadata;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários do value object {@link Metadata}.
 */
class MetadataTest {

    @Test
    void shouldCreateValidMetadata() {
        // arrange
        String userId = "user-1";
        String requestId = "request-1";

        // act
        Metadata metadata = new Metadata(userId, requestId);

        // assert
        assertEquals("user-1", metadata.userId());
        assertEquals("request-1", metadata.requestId());
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        // arrange
        String userId = null;
        String requestId = "request-1";

        // act / assert
        assertThrows(NullPointerException.class, () -> new Metadata(userId, requestId));
    }

    @Test
    void shouldThrowExceptionWhenRequestIdIsNull() {
        // arrange
        String userId = "user-1";
        String requestId = null;

        // act / assert
        assertThrows(NullPointerException.class, () -> new Metadata(userId, requestId));
    }
}
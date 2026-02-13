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
        // Arrange
        String userId = "user-1";
        String requestId = "request-1";

        // Act
        Metadata metadata = new Metadata(userId, requestId);

        // Assert
        assertEquals("user-1", metadata.userId());
        assertEquals("request-1", metadata.requestId());
    }

    @Test
    void shouldThrowExceptionWhenUserIdIsNull() {
        // Arrange
        String userId = null;
        String requestId = "request-1";

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new Metadata(userId, requestId));
    }

    @Test
    void shouldThrowExceptionWhenRequestIdIsNull() {
        // Arrange
        String userId = "user-1";
        String requestId = null;

        // Act & Assert
        assertThrows(NullPointerException.class, () -> new Metadata(userId, requestId));
    }
}
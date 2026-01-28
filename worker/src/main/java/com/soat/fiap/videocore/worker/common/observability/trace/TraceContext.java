package com.soat.fiap.videocore.worker.common.observability.trace;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;

import java.util.Map;

/**
 * Utilitário para acesso e enriquecimento do span atual via OpenTelemetry.
 *
 */
public class TraceContext {

    /**
     * Recupera o span atualmente ativo.
     *
     * @return span atual ou {@code null} se não houver tracing ativo
     */
    private static Span currentSpan() {
        return Span.current();
    }

    /**
     * Adiciona um evento simples ao span atual.
     *
     * @param name nome do evento
     */
    public static void addEvent(String name) {
        var span = currentSpan();

        if (span != null) {
            span.addEvent(name);
        }
    }

    /**
     * Adiciona um evento ao span atual com atributos.
     *
     * @param name       nome do evento
     * @param attributes atributos associados ao evento
     */
    public static void addEvent(String name, Map<String, Object> attributes) {
        var span = currentSpan();
        if (span != null) {
            var eventAttributesBuilder = Attributes.builder();

            attributes.forEach((key, value) -> {
                eventAttributesBuilder.put(key, value.toString());
            });

            span.addEvent(name, eventAttributesBuilder.build());
        }
    }

    /**
     * Adiciona um evento ao span atual registrando um único objeto como tag.
     *
     * @param name  nome do evento
     * @param value objeto a ser registrado como tag do evento
     */
    public static void addEvent(String name, Object value) {
        var span = currentSpan();

        if (span != null && value != null) {
            span.addEvent(
                    name,
                    Attributes.builder()
                            .put("value", value.toString())
                            .build()
            );
        }
    }
}
package com.soat.fiap.videocore.worker.infrastructure.common.observability.logback.appenders;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.opentelemetry.api.trace.Span;

/**
 * Appender Logback que adiciona mensagens de log como eventos
 * no span OpenTelemetry atualmente ativo.
 *
 * <p>Cada log é propagado para o trace via {@link io.opentelemetry.api.trace.Span#current()},
 * permitindo correlação entre logs e traces distribuídos.</p>
 */
public class OtelTracingLogAppender extends AppenderBase<ILoggingEvent> {

    /**
     * Adiciona a mensagem formatada do log como evento no span atual.
     *
     * @param event evento de log do Logback
     */
    @Override
    protected void append(ILoggingEvent event) {
        Span.current().addEvent(event.getFormattedMessage());
    }
}
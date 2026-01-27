package com.soat.fiap.videocore.worker.common.observability.trace.logback.appenders;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.opentelemetry.api.common.Attributes;
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
     * Adiciona a mensagem do log como evento no span atual.
     *
     * @param event evento de log do Logback
     */
    @Override
    protected void append(ILoggingEvent event) {

        var attributes = Attributes.builder()
                .put("log.severity", event.getLevel().toString())
                .put("log.message", event.getFormattedMessage())
                .put("log.logger", event.getLoggerName())
                .put("thread.name", event.getThreadName())
                .put("mdc", event.getMDCPropertyMap().toString())
                .build();

        Span.current().addEvent("log", attributes);

    }
}
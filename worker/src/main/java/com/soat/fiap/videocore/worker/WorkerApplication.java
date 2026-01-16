package com.soat.fiap.videocore.worker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication @Slf4j
public class WorkerApplication {

    static void main(String[] args) {

        SpringApplication.run(WorkerApplication.class, args);
	}

    @Bean
    public CommandLineRunner testSendMetric(MeterRegistry meterRegistry) {
        return args -> {
            Counter counter = Counter.builder("videocore.test.metric")
                    .description("Métrica de teste para validar envio OTLP")
                    .tag("component", "startup")
                    .tag("type", "manual")
                    .register(meterRegistry);

            counter.increment();
            counter.increment(2);

            log.info("Métrica videocore.test.metric enviada");
        };
    }


    @Bean
    public CommandLineRunner testSendTrace(Tracer tracer) {
        return args -> {
            Span span = tracer.nextSpan()
                    .name("videocore.test.trace")
                    .tag("component", "startup")
                    .tag("type", "manual");

            try (Tracer.SpanInScope scope = tracer.withSpan(span.start())) {
                // Simula algum trabalho
                Thread.sleep(100);
                span.event("processing.started");
                log.trace("processing.started começou");
                span.event("processing.finished");
            } catch (InterruptedException e) {
                span.error(e);
                Thread.currentThread().interrupt();
            } finally {
                span.end();
            }
        };
    }

}

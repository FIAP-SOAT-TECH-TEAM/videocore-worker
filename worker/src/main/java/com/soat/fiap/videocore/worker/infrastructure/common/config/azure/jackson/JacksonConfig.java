package com.soat.fiap.videocore.worker.infrastructure.common.config.azure.jackson;

import com.azure.spring.messaging.servicebus.implementation.support.converter.ServiceBusMessageConverter;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Jackson para conversão de mensagens do Azure Service Bus.
 */
@Configuration
public class JacksonConfig {

    /**
     * Define um {@link ServiceBusMessageConverter} com suporte a tipos {@code java.time}.
     *
     * @return conversor de mensagens configurado com {@link JavaTimeModule}
     */
    @Bean
    public ServiceBusMessageConverter serviceBusMessageConverter() {
        var jsonMapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

        return new ServiceBusMessageConverter(jsonMapper);
    }
}
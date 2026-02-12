package com.soat.fiap.videocore.worker.infrastructure.common.config.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Jackson para conversão de mensagens.
 */
@Configuration
public class JacksonConfig {

    /**
     * Cria um {@link ObjectMapper} configurado.
     * <p>Registra automaticamente módulos e desabilita serialização de datas como timestamps.</p>
     *
     * @return ObjectMapper configurado para a aplicação
     */
    @Bean
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }
}
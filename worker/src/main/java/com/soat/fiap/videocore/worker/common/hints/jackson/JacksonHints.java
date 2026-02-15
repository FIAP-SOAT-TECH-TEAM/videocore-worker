package com.soat.fiap.videocore.worker.common.hints.jackson;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.ProcessVideoStatusUpdateEventDto;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * RuntimeHints para suporte a serialização com Jackson
 * no módulo worker em ambiente GraalVM Native Image.
 *
 * <p>
 * Registra reflexão necessária para:
 * <ul>
 *     <li>DTOs serializados via ObjectMapper</li>
 *     <li>Tipos do Azure Service Bus utilizados no envio</li>
 * </ul>
 *
 * <p>
 * Necessário pois o Jackson utiliza reflexão para acessar
 * construtores, campos e métodos durante serialização.
 */
public class JacksonHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        registerJacksonType(hints, ProcessVideoStatusUpdateEventDto.class);
        registerJacksonType(hints, ServiceBusMessage.class);
        registerJacksonType(hints, ServiceBusSenderClient.class);
        registerJacksonType(hints, ObjectMapper.class);
    }

    private void registerJacksonType(RuntimeHints hints, Class<?> type) {
        hints.reflection().registerType(
                type,
                MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_PUBLIC_METHODS,
                MemberCategory.INVOKE_DECLARED_METHODS,
                MemberCategory.ACCESS_DECLARED_FIELDS
        );
    }
}
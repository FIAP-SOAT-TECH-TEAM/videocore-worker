package com.soat.fiap.videocore.worker.common.hints.jackson;

import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.ProcessVideoStatusUpdateEventDto;
import com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.payload.BlobCreatedCloudEventSchemaPayload;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * Registrar de {@link RuntimeHints} responsável por registrar metadados de reflexão
 * para as classes utilizadas na desserialização/serialização via Jackson em tempo
 * de execução quando a aplicação é compilada como Native Image (GraalVM).
 *
 * <p>
 * Este registrar equivale funcionalmente ao uso da annotation:
 * </p>
 *
 * <pre>
 * {@code
 * @RegisterReflectionForBinding({
 *     BlobCreatedCloudEventSchemaPayload.class,
 *     BlobCreatedCloudEventSchemaPayload.DataPayload.class,
 *     BlobCreatedCloudEventSchemaPayload.DataPayload.StorageDiagnostics.class,
 *     ProcessVideoStatusUpdateEventDto.class
 * })
 * }
 * </pre>
 *
 * <p>
 * Ao invés de utilizar a annotation, esta abordagem programática registra
 * explicitamente as categorias de membros necessárias para que o Jackson
 * consiga instanciar, acessar construtores, campos e métodos das classes
 * durante a execução nativa.
 * </p>
 *
 * <p>
 * Deve ser registrada via {@code @ImportRuntimeHints(JacksonHints.class)}
 * ou declarada como bean em configuração para que o Spring AOT a processe.
 * </p>
 */
public class JacksonHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        registerForBinding(hints, BlobCreatedCloudEventSchemaPayload.class);
        registerForBinding(hints, BlobCreatedCloudEventSchemaPayload.DataPayload.class);
        registerForBinding(hints, BlobCreatedCloudEventSchemaPayload.DataPayload.StorageDiagnostics.class);
        registerForBinding(hints, ProcessVideoStatusUpdateEventDto.class);
    }

    private void registerForBinding(RuntimeHints hints, Class<?> type) {
        ReflectionHints reflectionHints = hints.reflection();

        reflectionHints.registerType(type,
                MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
                MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                MemberCategory.INVOKE_DECLARED_METHODS,
                MemberCategory.INVOKE_PUBLIC_METHODS,
                MemberCategory.ACCESS_DECLARED_FIELDS,
                MemberCategory.ACCESS_PUBLIC_FIELDS);
    }
}
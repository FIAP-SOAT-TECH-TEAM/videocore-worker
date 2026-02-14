package com.soat.fiap.videocore.worker.common.hints.azure;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * Registrar de {@link RuntimeHints} para suporte a reflexão em tempo de execução
 * quando a aplicação é compilada como imagem nativa (GraalVM).
 *
 * <p>
 * Registra explicitamente construtores, métodos e campos necessários da classe
 * {@code BlobStorageExceptionInternal}, evitando falhas na criação e
 * manipulação de exceções da Azure Blob Storage durante execução em ambiente AOT.
 * </p>
 */
public class BlobHints implements RuntimeHintsRegistrar {

    /**
     * Registra os hints de reflexão necessários para o correto funcionamento
     * da Azure Blob Storage SDK em modo nativo.
     *
     * @param hints estrutura utilizada pelo Spring AOT para registrar metadados
     *              necessários em tempo de execução
     * @param classLoader class loader da aplicação
     */
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(
                com.azure.storage.blob.implementation.models.BlobStorageExceptionInternal.class,
                MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
                MemberCategory.INVOKE_PUBLIC_METHODS,
                MemberCategory.ACCESS_DECLARED_FIELDS
        );
    }
}
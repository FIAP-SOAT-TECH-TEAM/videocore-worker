package com.soat.fiap.videocore.worker.common.observability.log;

import org.slf4j.MDC;

/**
 * Utilitário para gerenciamento de contexto canônico de logs via MDC.
 *
 * <p>Permite adicionar atributos, registrar erros e limpar o contexto
 * associado à thread corrente.</p>
 */
public class CanonicalContext {

    /**
     * Adiciona uma chave e valor ao MDC.
     *
     * @param key   nome do atributo
     * @param value valor associado
     */
    public static void add(String key, Object value) {
        MDC.put(key, String.valueOf(value));
    }

    /**
     * Remove todos os atributos do MDC.
     */
    public static void clear() {
        MDC.clear();
    }
}
package com.soat.fiap.videocore.worker.common.observability.trace;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para criação explícita de spans de tracing em métodos.
 *
 * <p>
 * Permite definir o nome do span e tags opcionais no formato
 * {@code chave=valor}. O span é criado automaticamente ao entrar no método
 * anotado e finalizado ao sair, incluindo cenários de exceção.
 * </p>
 *
 * <p>
 * O span criado torna-se o span atual, permitindo:
 * </p>
 * <ul>
 * <li>Adicionar eventos manualmente via
 * {@code Tracer.currentSpan().event(...)}</li>
 * <li>Registro automático de erro quando exceções são lançadas</li>
 * </ul>
 *
 * <p>
 * Exemplo de uso:
 * </p>
 *
 * <pre>{@code
 * @WithSpan(name = "teste.videocoore.span")
 * public void testSendTrace() {
 * 	try {
 * 		Thread.sleep(100);
 *
 * 		var currentSpan = tracer.currentSpan();
 * 		currentSpan.event("processing.started");
 * 		log.trace("processing.started começou");
 *
 * 		currentSpan.event("processing.finished");
 * 		throw new RuntimeException("TESTE");
 * 	} catch (InterruptedException e) {
 * 		Thread.currentThread().interrupt();
 * 		throw new RuntimeException(e);
 * 	}
 * }
 * }</pre>
 */
@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
public @interface WithSpan {

	/**
	 * Nome do span a ser criado.
	 */
	String name();

	/**
	 * Tags adicionais no formato {@code chave=valor}.
	 */
	String[] tags() default {};
}

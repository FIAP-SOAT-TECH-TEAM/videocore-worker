package com.soat.fiap.videocore.worker.common.observability.trace;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;

/**
 * Aspect que intercepta métodos anotados com {@link WithSpan} e gerencia o
 * ciclo de vida do span Micrometer.
 */
@Aspect @Component @RequiredArgsConstructor
public class WithSpanTracingAspect {

	private final Tracer tracer;

	@Around("@annotation(com.soat.fiap.videocore.worker.common.observability.trace.WithSpan)")
	public Object trace(ProceedingJoinPoint pjp) throws Throwable {
		var signature = (MethodSignature) pjp.getSignature();
		var method = signature.getMethod();
		var withSpan = method.getAnnotation(WithSpan.class);

		var span = tracer.nextSpan().name(withSpan.name());

		for (var tag : withSpan.tags()) {
			var parts = tag.split("=", 2);
			if (parts.length == 2) {
				span.tag(parts[0], parts[1]);
			}
		}

		try (Tracer.SpanInScope scope = tracer.withSpan(span.start())) {
			return pjp.proceed();
		} catch (Throwable ex) {
			span.error(ex);
			throw ex;
		} finally {
			span.end();
		}
	}
}

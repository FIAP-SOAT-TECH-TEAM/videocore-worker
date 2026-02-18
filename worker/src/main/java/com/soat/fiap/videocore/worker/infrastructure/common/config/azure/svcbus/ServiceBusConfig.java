package com.soat.fiap.videocore.worker.infrastructure.common.config.azure.svcbus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderClient;

/**
 * Configuração do Azure Service Bus.
 * <p>
 * Define beans para envio de mensagens e mantém nomes de filas/tópicos usados
 * pelo microsserviço.
 * </p>
 */
@Configuration
public class ServiceBusConfig {

	@Value("${azure.service-bus.connection-string}")
	private String connectionString;

	/**
	 * Fila principal de processamento de eventos.
	 * <p>
	 * Responsável pelo fluxo normal de processamento.
	 * </p>
	 */
	public static final String PROCESS_QUEUE = "process.queue";

	/**
	 * Tópico de status do processamento.
	 * <p>
	 * Publica atualizações de estado dos processos.
	 * </p>
	 */
	public static final String PROCESS_STATUS_TOPIC = "process.status.topic";

	/**
	 * Cria e configura beans do Service Bus para envio de mensagens.
	 */
	@Bean
	public ServiceBusClientBuilder serviceBusClientBuilder() {
		return new ServiceBusClientBuilder().connectionString(connectionString);
	}

	/**
	 * Cria um ServiceBusSenderClient para enviar mensagens de erro para a fila de
	 * erros.
	 *
	 * @param builder
	 *            builder do Service Bus
	 * @return client configurado para envio de erros
	 */
	@Bean
	public ServiceBusSenderClient processStatusSender(ServiceBusClientBuilder builder) {
		return builder.sender().topicName(PROCESS_STATUS_TOPIC).buildClient();
	}

}

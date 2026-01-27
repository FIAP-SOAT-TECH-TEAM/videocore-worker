package com.soat.fiap.videocore.worker;

import com.azure.spring.messaging.implementation.annotation.EnableAzureMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication @Slf4j @EnableAzureMessaging
public class WorkerApplication {

    static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
	}

}

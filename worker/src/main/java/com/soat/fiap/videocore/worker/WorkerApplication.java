package com.soat.fiap.videocore.worker;

import com.soat.fiap.videocore.worker.common.hints.azure.BlobHints;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(BlobHints.class)
public class WorkerApplication {

    static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
	}

}

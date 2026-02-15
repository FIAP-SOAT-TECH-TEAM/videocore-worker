package com.soat.fiap.videocore.worker;

import com.soat.fiap.videocore.worker.common.hints.azure.BlobHints;
import com.soat.fiap.videocore.worker.core.interfaceadapters.dto.ProcessVideoStatusUpdateEventDto;
import com.soat.fiap.videocore.worker.infrastructure.in.event.azsvcbus.payload.BlobCreatedCloudEventSchemaPayload;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;

@SpringBootApplication
@ImportRuntimeHints(BlobHints.class)
@RegisterReflectionForBinding({BlobCreatedCloudEventSchemaPayload.class, ProcessVideoStatusUpdateEventDto.class})
public class WorkerApplication {

    static void main(String[] args) {
        SpringApplication.run(WorkerApplication.class, args);
	}

}

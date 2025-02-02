package com.anmoma.englishdaily;

import com.corundumstudio.socketio.BroadcastOperations;
import com.corundumstudio.socketio.SocketIOServer;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import static org.mockito.BDDMockito.given;

@TestConfiguration(proxyBeanMethods = false)
public class TestEnvironmentConfiguration {

    //    @Bean
    //    @ServiceConnection
    //    OllamaContainer ollamaContainer() {
    //        return new OllamaContainer(DockerImageName.parse("ollama/ollama:latest"));
    //    }

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> pgvectorContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("pgvector/pgvector:pg16"));
    }

    @Bean
    @Primary
    BroadcastOperations broadcastOperations() {
        return Mockito.mock(BroadcastOperations.class);
    }

    @Bean
    @Primary
    SocketIOServer socketIOServer(BroadcastOperations broadcastOperations) {
        SocketIOServer socketIOServer = Mockito.mock(SocketIOServer.class);
        given(socketIOServer.getBroadcastOperations()).willReturn(broadcastOperations);
        return socketIOServer;
    }
}

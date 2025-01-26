package com.anmoma.englishdaily.askllama;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class AskLlamaSocketInitiator {
    private static final Logger log = LoggerFactory.getLogger(AskLlamaSocketInitiator.class);

    private final SocketIOServer socketIOServer;

    AskLlamaSocketInitiator(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
    }

    @PostConstruct
    void startServer() {
        socketIOServer.addConnectListener(client -> {
            log.info("Client connected: {}", client.getSessionId());
        });
        log.info("Starting AskLlama socket server");
        socketIOServer.start();
    }
}

package com.anmoma.englishdaily.askllama;

import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(SocketIOServer.class)
public class AskLlamaWebsocketConfiguration {

    @Bean
    public SocketIOServer socketIOServer(@Value("${englishdaily.socketio.server.host}") String hostname,
            @Value("${englishdaily.socketio.server.port}") int port, @Value("${englishdaily.socketio.server.context}") String context) {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname(hostname);
        config.setPort(port);
        config.setContext(context);
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setReuseAddress(true);
        config.setSocketConfig(socketConfig);
        config.setRandomSession(true);
        return new SocketIOServer(config);
    }

}

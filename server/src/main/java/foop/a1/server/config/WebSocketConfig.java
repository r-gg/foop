package foop.a1.server.config;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final Logger logger;

    @Autowired
    public WebSocketConfig(Logger logger){
        this.logger = logger;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        logger.info("registering stomp endpoints");
        registry.addEndpoint("/game").setHandshakeHandler(new CustomHandshakeHandler());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app");
        config.enableSimpleBroker("/queue", "/topic");
        config.setUserDestinationPrefix("/user");
    }
}

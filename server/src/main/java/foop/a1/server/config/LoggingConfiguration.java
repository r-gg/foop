package foop.a1.server.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.invoke.MethodHandles;

@Configuration
public class LoggingConfiguration {

    // todo: maybe return logger to each class, because now each time something is logged with it, it says that it comes from logging configuration
    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    }
}

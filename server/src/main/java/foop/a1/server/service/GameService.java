package foop.a1.server.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final Logger logger;

    @Autowired
    public GameService(Logger logger) {
        this.logger = logger;
    }
}

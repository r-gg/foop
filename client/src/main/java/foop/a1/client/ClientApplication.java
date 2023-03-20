package foop.a1.client;

import foop.a1.client.main.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class ClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startGame(){
		System.setProperty("java.awt.headless", "false");
		new Game();
	}

}
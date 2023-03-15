package foop.assignment1;

import foop.assignment1.main.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Assignment1Application {

	public static void main(String[] args) {
		SpringApplication.run(Assignment1Application.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void startGame(){
		System.setProperty("java.awt.headless", "false");
		new Game();
	}

}

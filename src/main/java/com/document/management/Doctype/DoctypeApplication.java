package com.document.management.Doctype;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DoctypeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctypeApplication.class, args);
	}

	@Bean
	public ChatClient chatClient(OllamaChatModel model) {
		return ChatClient.create(model);
	}

}

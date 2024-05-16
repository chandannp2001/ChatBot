package com.ChatBot.Controller;

import org.springframework.ai.chat.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ManiController {

	private ChatClient chatClient;
	
	public ManiController(ChatClient chatClient) {

		this.chatClient = chatClient;
	}
	
	@GetMapping("/dad-jokes")
	public String generate(@RequestParam(value = "message",defaultValue = "Tell me a dad joke") String message) {
		System.out.println("dad");
		return chatClient.call(message);
	}
}

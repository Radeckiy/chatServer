package com.radeckiy.chat.controllers;

import com.radeckiy.chat.models.Message;
import com.radeckiy.chat.models.MessageType;
import com.radeckiy.chat.repositories.MessageRepository;
import org.springframework.core.env.Environment;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private MessageRepository repository;

    public WebSocketController(MessageRepository repository, Environment env) {
        this.repository = repository;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/publicChatRoom")
    public Message sendMessage(@Payload Message message) {
        if(message.getType() == MessageType.CHAT)
            repository.save(message);

        return message;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/publicChatRoom")
    public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender());
        return sendMessage(message);
    }
}

package com.maq.mindmate.controllers;

import com.maq.mindmate.models.ChatMessage;
import com.maq.mindmate.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    @Autowired
    private final ChatService chatService;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage message) {
        ChatMessage savedMessage = chatService.processMessage(message);
        messagingTemplate.convertAndSend("/topic/chat." + message.getRoom(), savedMessage);
    }
}


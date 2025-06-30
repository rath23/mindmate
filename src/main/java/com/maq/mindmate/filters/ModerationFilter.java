package com.maq.mindmate.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maq.mindmate.models.ChatMessage;
import com.maq.mindmate.services.ModerationService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ModerationFilter implements ChannelInterceptor {
    private final ModerationService moderationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ModerationFilter(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() != null &&
                accessor.getDestination() != null &&
                accessor.getDestination().equals("/app/chat.send")) {

            try {
                // Convert byte[] payload to ChatMessage
                byte[] payload = (byte[]) message.getPayload();
                ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);

                if (moderationService.containsBadWords(chatMessage.getContent())) {
                    throw new IllegalArgumentException("Blocked: Inappropriate content");
                }
            } catch (IOException e) {
                throw new RuntimeException("Message parsing failed", e);
            }
        }
        return message;
    }
}
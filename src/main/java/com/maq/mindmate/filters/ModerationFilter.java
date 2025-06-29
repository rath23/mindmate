package com.maq.mindmate.filters;

// ModerationFilter.java (Optional interceptor)
import com.maq.mindmate.models.ChatMessage;
import com.maq.mindmate.services.ModerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class ModerationFilter implements ChannelInterceptor {
    @Autowired
    private final ModerationService moderationService;

    public ModerationFilter(ModerationService moderationService) {
        this.moderationService = moderationService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() != null &&
                accessor.getDestination() != null &&
                accessor.getDestination().equals("/app/chat.send")) {

            ChatMessage chatMessage = (ChatMessage) message.getPayload();
            if (moderationService.containsBadWords(chatMessage.getContent())) {
                throw new IllegalArgumentException("Blocked: Inappropriate content");
            }
        }
        return message;
    }
}

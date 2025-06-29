package com.maq.mindmate.services;

import com.maq.mindmate.models.BlockedUser;
import com.maq.mindmate.models.ChatMessage;
import com.maq.mindmate.repository.BlockedUserRepository;
import com.maq.mindmate.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Autowired
    private final ChatMessageRepository messageRepository;
    @Autowired
    private final BlockedUserRepository blockedUserRepository;
    @Autowired
    private final ModerationService moderationService;

    @Transactional
    public ChatMessage processMessage(ChatMessage message) {
        // Check if user is blocked
        BlockedUser blockedUser = blockedUserRepository.findByNicknameAndRoom(
                message.getSenderNickname(), message.getRoom()
        ).orElse(null);

        if (blockedUser != null && blockedUser.getBlockedUntil().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("User is blocked in this room");
        }

        // Content moderation
        if (moderationService.containsBadWords(message.getContent())) {
            throw new IllegalArgumentException("Message contains inappropriate content");
        }

        return messageRepository.save(message);
    }
}

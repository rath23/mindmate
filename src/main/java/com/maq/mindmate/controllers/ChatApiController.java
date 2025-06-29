package com.maq.mindmate.controllers;

import com.maq.mindmate.models.ChatMessage;
import com.maq.mindmate.repository.ChatMessageRepository;
import com.maq.mindmate.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatApiController {
    @Autowired
    private final ChatMessageRepository messageRepository;
    @Autowired
    private final ReportService reportService;

    @GetMapping("/messages/{room}")
    public List<ChatMessage> getMessages(@PathVariable String room) {
        return messageRepository.findByRoom(room);
    }

    @PostMapping("/report")
    public void reportUser(@RequestParam String reporter,
                           @RequestParam String reported,
                           @RequestParam String room) {
        reportService.handleReport(reporter, reported, room);
    }
}

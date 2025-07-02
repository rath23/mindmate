package com.maq.mindmate.controllers;

import com.maq.mindmate.dto.JournalDTO;
import com.maq.mindmate.models.User;
import com.maq.mindmate.services.AuthService;
import com.maq.mindmate.services.JournalService;
import com.maq.mindmate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/journal")
@RequiredArgsConstructor
public class JournalController {

    @Autowired
    private final JournalService journalService;

    @Autowired
    private  final UserService userService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody JournalDTO request, Authentication auth) {
        User user = userService.getCurrentUserDetailWithAuth(auth);
        return journalService.createEntry(request, user);
    }

    @GetMapping
    public ResponseEntity<?> getAll(Authentication auth) {
        User user = userService.getCurrentUserDetailWithAuth(auth);
        return ResponseEntity.ok(journalService.getAllEntries(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable UUID id, Authentication auth) {
        User user = userService.getCurrentUserDetailWithAuth(auth);
        return ResponseEntity.of(journalService.getEntry(id, user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody JournalDTO request, Authentication auth) {
        User user = userService.getCurrentUserDetailWithAuth(auth);
        return ResponseEntity.ok(journalService.updateEntry(id, request, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id, Authentication auth) {
        User user = userService.getCurrentUserDetailWithAuth(auth);
        journalService.deleteEntry(id, user);
        return ResponseEntity.ok("Entry deleted.");
    }
}


package com.maq.mindmate.services;

import com.maq.mindmate.dto.JournalDTO;
import com.maq.mindmate.exceptions.JournalEntryNotFoundException;
import com.maq.mindmate.models.JournalEntry;
import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JournalService {


    @Autowired
    private final JournalEntryRepository journalEntryRepository;



    public ResponseEntity<?> createEntry(JournalDTO request, User user) {
        JournalEntry entry = new JournalEntry();
        entry.setHeading(request.getHeading());
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());
        entry.setBody(request.getBody());
        entry.setUser(user);
        journalEntryRepository.save(entry);
        return ResponseEntity.ok().body("Journal was created");
    }

    public List<JournalDTO> getAllEntries(User user) {
        List<JournalEntry> entries = journalEntryRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        List<JournalDTO> dtoList = new ArrayList<>();
        for (JournalEntry entry : entries) {
            JournalDTO dto = new JournalDTO();
            BeanUtils.copyProperties(entry, dto);
            dtoList.add(dto);
        }
        return dtoList;

    }

    public Optional<JournalEntry> getEntry(UUID id, User user) {
        return journalEntryRepository.findById(id)
                .filter(entry -> entry.getUser().getId().equals(user.getId()));
    }

    public JournalDTO updateEntry(UUID id, JournalDTO request, User user) {
        JournalEntry entry = getEntry(id, user)
                .orElseThrow(() -> new JournalEntryNotFoundException("Entry not found for this user"));


        entry.setHeading(request.getHeading());
        entry.setUpdatedAt(LocalDateTime.now());
        entry.setBody(request.getBody());

        JournalEntry savedEntry = journalEntryRepository.save(entry);

        JournalDTO response = new JournalDTO();
        BeanUtils.copyProperties(savedEntry, response);
        return response;
    }


    public void deleteEntry(UUID id, User user) {
        JournalEntry entry = getEntry(id, user).orElseThrow(() -> new JournalEntryNotFoundException("Entry not found for this user"));
        journalEntryRepository.delete(entry);
    }
}

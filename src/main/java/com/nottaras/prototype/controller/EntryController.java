package com.nottaras.prototype.controller;

import com.nottaras.prototype.dto.EntryDto;
import com.nottaras.prototype.dto.UpsertEntryDto;
import com.nottaras.prototype.service.EntryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/moods")
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<EntryDto> createEntry(@RequestBody UpsertEntryDto createDto) {
        var createdEntry = entryService.createEntry(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }

    @GetMapping
    public ResponseEntity<List<EntryDto>> getAllMoodEntries() {
        var entries = entryService.getEntries();
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntryDto> getEntryById(@PathVariable Long id) {
        var entry = entryService.getEntryById(id);
        return entry.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntryDto> updateEntry(@PathVariable Long id, @RequestBody UpsertEntryDto upsertDto) {
        try {
            var updatedEntry = entryService.updateEntry(id, upsertDto);
            return ResponseEntity.ok(updatedEntry);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        entryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }

}

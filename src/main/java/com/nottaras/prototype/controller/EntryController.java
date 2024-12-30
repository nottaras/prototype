package com.nottaras.prototype.controller;

import com.nottaras.prototype.dto.EntryDto;
import com.nottaras.prototype.dto.UpsertEntryDto;
import com.nottaras.prototype.service.EntryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moods")
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<EntryDto> createEntry(@RequestBody UpsertEntryDto createDto,
                                                @AuthenticationPrincipal UUID userId) {
        var createdEntry = entryService.createEntry(createDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }

    @GetMapping
    public ResponseEntity<List<EntryDto>> getAllEntries(@AuthenticationPrincipal UUID userId) {
        var entries = entryService.getEntries(userId);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntryDto> getEntryById(@PathVariable Long id,
                                                 @AuthenticationPrincipal UUID userId) {
        var entry = entryService.getEntryById(id, userId);
        return entry.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntryDto> updateEntry(@PathVariable Long id,
                                                @RequestBody UpsertEntryDto upsertDto,
                                                @AuthenticationPrincipal UUID userId) {
        try {
            var updatedEntry = entryService.updateEntry(id, upsertDto, userId);
            return ResponseEntity.ok(updatedEntry);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id,
                                            @AuthenticationPrincipal UUID userId) {
        entryService.deleteEntry(id, userId);
        return ResponseEntity.noContent().build();
    }

}

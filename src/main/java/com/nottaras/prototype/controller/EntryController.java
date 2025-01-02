package com.nottaras.prototype.controller;

import com.nottaras.prototype.auth.HasRoleUser;
import com.nottaras.prototype.dto.EntryDto;
import com.nottaras.prototype.dto.UpsertEntryDto;
import com.nottaras.prototype.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@HasRoleUser
@RequestMapping("/api/v1/moods")
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntryDto createEntry(@RequestBody UpsertEntryDto createDto, @AuthenticationPrincipal UUID userId) {
        return entryService.createEntry(createDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EntryDto> getAllEntries(@AuthenticationPrincipal UUID userId) {
        return entryService.getEntries(userId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntryDto getEntryById(@PathVariable Long id, @AuthenticationPrincipal UUID userId) {
        return entryService.getEntryById(id, userId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntryDto updateEntry(@PathVariable Long id,
                                @RequestBody UpsertEntryDto upsertDto,
                                @AuthenticationPrincipal UUID userId) {
        return entryService.updateEntry(id, upsertDto, userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEntry(@PathVariable Long id, @AuthenticationPrincipal UUID userId) {
        entryService.deleteEntry(id, userId);
    }

}

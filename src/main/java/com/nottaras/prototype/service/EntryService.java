package com.nottaras.prototype.service;

import com.nottaras.prototype.dto.EntryDto;
import com.nottaras.prototype.dto.UpsertEntryDto;
import com.nottaras.prototype.mapper.EntryMapper;
import com.nottaras.prototype.repository.EntryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;
    private final EntryMapper entryMapper;

    @Transactional
    public EntryDto createEntry(UpsertEntryDto createDto, UUID userId) {
        var entry = entryMapper.map(createDto);
        entry.setUserId(userId);
        return entryMapper.map(entryRepository.save(entry));
    }

    @Transactional(readOnly = true)
    public List<EntryDto> getEntries(UUID userId) {
        return entryRepository.findAllByUserId(userId).stream()
                .map(entryMapper::map)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<EntryDto> getEntryById(Long id, UUID userId) {
        return entryRepository.findByIdAndUserId(id, userId)
                .map(entryMapper::map);
    }

    @Transactional
    public EntryDto updateEntry(Long id, UpsertEntryDto createDto, UUID userId) {
        var existingEntry = entryRepository.findByIdAndUserId(id, userId).orElseThrow(EntityNotFoundException::new);
        entryMapper.update(createDto, existingEntry);

        return entryMapper.map(entryRepository.save(existingEntry));
    }

    @Transactional
    public void deleteEntry(Long id, UUID userId) {
        entryRepository.deleteByIdAndUserId(id, userId);
    }
}
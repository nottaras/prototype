package com.nottaras.prototype.service;

import com.nottaras.prototype.dto.EntryDto;
import com.nottaras.prototype.dto.UpsertEntryDto;
import com.nottaras.prototype.mapper.EntryMapper;
import com.nottaras.prototype.repository.EntryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;
    private final EntryMapper entryMapper;

    public EntryDto createEntry(UpsertEntryDto createDto) {
        var entry = entryMapper.map(createDto);
        return entryMapper.map(entryRepository.save(entry));
    }

    public List<EntryDto> getEntries() {
        return entryRepository.findAll().stream()
                .map(entryMapper::map)
                .toList();
    }

    public Optional<EntryDto> getEntryById(Long id) {
        return entryRepository.findById(id)
                .map(entryMapper::map);
    }

    public EntryDto updateEntry(Long id, UpsertEntryDto createDto) {
        var existingMoodEntry = entryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        entryMapper.update(createDto, existingMoodEntry);

        return entryMapper.map(entryRepository.save(existingMoodEntry));
    }

    public void deleteEntry(Long id) {
        entryRepository.deleteById(id);
    }
}
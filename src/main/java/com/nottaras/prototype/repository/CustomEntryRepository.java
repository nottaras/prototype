package com.nottaras.prototype.repository;

import com.nottaras.prototype.dto.FilterEntryDto;
import com.nottaras.prototype.model.Entry;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface CustomEntryRepository {

    Page<Entry> findByFilter(UUID userId, FilterEntryDto filterDto);
}

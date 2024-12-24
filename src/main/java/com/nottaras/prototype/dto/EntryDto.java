package com.nottaras.prototype.dto;

import com.nottaras.prototype.enums.Mood;

import java.time.LocalDate;

public record EntryDto(Long id, Mood mood, LocalDate date) {
}

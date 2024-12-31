package com.nottaras.prototype.dto;

import com.nottaras.prototype.enums.Mood;

import java.time.LocalDate;

public record UpsertEntryDto(Mood mood, LocalDate date, String notes) {
}

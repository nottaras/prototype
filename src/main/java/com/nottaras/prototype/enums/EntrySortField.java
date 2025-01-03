package com.nottaras.prototype.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EntrySortField implements SortField {
    DATE("date");

    private final String field;
}

package com.nottaras.prototype.dto;

import com.nottaras.prototype.enums.EntrySortField;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FilterEntryDto extends PageableDto<EntrySortField> {

    private LocalDate from;
    private LocalDate to;
}

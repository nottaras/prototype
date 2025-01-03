package com.nottaras.prototype.dto;

import com.nottaras.prototype.enums.SortField;
import com.querydsl.core.types.Order;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public abstract class PageableDto<F extends Enum<F> & SortField> {

    @PositiveOrZero
    private int pageNumber;

    @Min(1)
    @Max(99)
    private int pageSize;

    private F sortField;

    private Order sortOrder;
}

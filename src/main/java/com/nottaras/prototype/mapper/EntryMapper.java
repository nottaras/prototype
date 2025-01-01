package com.nottaras.prototype.mapper;

import com.nottaras.prototype.dto.EntryDto;
import com.nottaras.prototype.dto.UpsertEntryDto;
import com.nottaras.prototype.model.Entry;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface EntryMapper {

    EntryDto map(Entry entity);

    Entry map(EntryDto dto);

    Entry map(UpsertEntryDto createDto);

    void update(UpsertEntryDto dto, @MappingTarget Entry entity);
}

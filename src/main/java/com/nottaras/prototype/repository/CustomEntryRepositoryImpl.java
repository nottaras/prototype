package com.nottaras.prototype.repository;

import com.nottaras.prototype.dto.FilterEntryDto;
import com.nottaras.prototype.enums.SortField;
import com.nottaras.prototype.model.Entry;
import com.nottaras.prototype.model.QEntry;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class CustomEntryRepositoryImpl implements CustomEntryRepository {

    public static final QEntry ENTRY = QEntry.entry;

    private final EntityManager entityManager;

    @Override
    public Page<Entry> findByFilter(UUID userId, FilterEntryDto filterDto) {
        var queryFactory = new JPAQueryFactory(entityManager);

        var orderSpecifier = buildOrderSpecifier(filterDto.getSortOrder(), filterDto.getSortField());

        var pageable = PageRequest.of(filterDto.getPageNumber(), filterDto.getPageSize());

        var predicate = ENTRY.date.between(filterDto.getFrom(), filterDto.getTo())
            .and(ENTRY.userId.eq(userId));

        var entries = queryFactory.selectFrom(ENTRY)
            .where(predicate)
            .orderBy(orderSpecifier)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = Optional.ofNullable(
                queryFactory.select(ENTRY.id.count())
                    .from(ENTRY)
                    .where(predicate)
                    .fetchOne())
            .orElse(0L);

        return new PageImpl<>(entries, pageable, total);
    }

    private OrderSpecifier<?> buildOrderSpecifier(Order order, SortField sortField) {
        Path<Object> fieldPath = Expressions.path(Object.class, ENTRY, sortField.getField());
        return new OrderSpecifier(order, fieldPath);
    }
}

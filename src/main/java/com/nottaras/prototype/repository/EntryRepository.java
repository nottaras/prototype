package com.nottaras.prototype.repository;

import com.nottaras.prototype.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    Optional<Entry> findByIdAndUserId(Long id, UUID userId);

    List<Entry> findAllByUserId(UUID userId);

    void deleteByIdAndUserId(Long id, UUID userId);
}

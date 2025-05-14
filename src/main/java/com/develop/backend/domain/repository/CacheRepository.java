package com.develop.backend.domain.repository;

import com.develop.backend.domain.entity.Cache;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface CacheRepository extends JpaRepository<Cache, String> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Cache c WHERE c.createdAtUtc < :cutoff")
    void deleteOlderThan(@Param("cutoff") Timestamp cutoff);
}
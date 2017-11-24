package com.ss.test.simple.ms.cache.db.repository;

import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

/**
 * @author JavaSaBr
 */
public interface KeyValueRepository extends CrudRepository<KeyValueEntity, String> {

    void deleteByExpiredTimeBefore(@NotNull final LocalDateTime time);
}

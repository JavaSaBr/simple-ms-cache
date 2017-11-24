package com.ss.test.simple.ms.cache.db.repository;

import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author JavaSaBr
 */
public interface KeyValueRepository extends CrudRepository<KeyValueEntity, String> {
}

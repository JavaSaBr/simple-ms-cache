package com.ss.test.simple.ms.cache.service;

import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import com.ss.test.simple.ms.cache.dto.KeyValueObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author JavaSaBr
 */
public interface KeyValueService {

    @Nullable KeyValueEntity get(@NotNull String key);

    void set(@NotNull final KeyValueObject object);
}

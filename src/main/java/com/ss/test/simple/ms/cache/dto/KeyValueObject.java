package com.ss.test.simple.ms.cache.dto;

import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author JavaSaBr
 */
public class KeyValueObject {

    @Nullable
    private String key;

    @Nullable
    private String value;

    public KeyValueObject() {
    }

    public KeyValueObject(@Nullable final String key, @Nullable final String value) {
        this.key = key;
        this.value = value;
    }

    public KeyValueObject(@NotNull final KeyValueEntity entity) {
        this.key = entity.getKey();
        this.value = entity.getValue();
    }

    public @Nullable String getKey() {
        return key;
    }

    public @Nullable String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "KeyValueObject{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }
}

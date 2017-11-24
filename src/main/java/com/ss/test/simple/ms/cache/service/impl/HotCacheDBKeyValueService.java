package com.ss.test.simple.ms.cache.service.impl;

import com.ss.test.simple.ms.cache.SimpleMsCacheApplication;
import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import com.ss.test.simple.ms.cache.db.repository.KeyValueRepository;
import com.ss.test.simple.ms.cache.dto.KeyValueObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author JavaSaBr
 */
@Service("keyValueService")
public class HotCacheDBKeyValueService extends DBKeyValueService {

    private static final KeyValueEntity NULL_ENTITY = new KeyValueEntity();

    @NotNull
    private final ConcurrentMap<@NotNull String, @Nullable KeyValueEntity> hotEntities;

    @Autowired
    public HotCacheDBKeyValueService(@NotNull final KeyValueRepository keyValueRepository,
                                     @NotNull final SimpleMsCacheApplication application) {
        super(keyValueRepository, application);
        this.hotEntities = new ConcurrentHashMap<>();
    }

    @Override
    public @Nullable KeyValueEntity get(@NotNull final String key) {

        KeyValueEntity hotEntity = tryTakeHotEntity(key);

        if (hotEntity == NULL_ENTITY) {
            return null;
        } else if (hotEntity != null) {
            return hotEntity;
        }

        cleanHotEntities();

        hotEntity = super.get(key);

        if (hotEntity == null) {
            hotEntities.putIfAbsent(key, NULL_ENTITY);
        } else {
            hotEntities.putIfAbsent(key, hotEntity);
        }

        return hotEntity;
    }

    @Override
    protected void clean() {
        super.clean();
        cleanHotEntities();
    }

    private void cleanHotEntities() {

        final int maxSize = getApplication().getHotCacheMaxSize();

        if (hotEntities.size() >= maxSize) {
            hotEntities.clear();
        }
    }

    protected @Nullable KeyValueEntity tryTakeHotEntity(@NotNull final String key) {

        final KeyValueEntity entity = hotEntities.get(key);
        if (entity == null) {
            return null;
        } else if (entity == NULL_ENTITY) {
            return entity;
        }

        final LocalDateTime expiredTime = entity.getExpiredTime();
        if (expiredTime.isBefore(LocalDateTime.now())) {
            hotEntities.remove(key, entity);
            return null;
        }

        return entity;
    }

    @Override
    protected void insertEntity(@NotNull final KeyValueObject object) {
        super.insertEntity(object);
        hotEntities.remove(object.getKey(), NULL_ENTITY);
    }

    @Override
    public @NotNull KeyValueEntity handleChanges(@NotNull final KeyValueEntity entity,
                                                    @NotNull final KeyValueObject object) {

        final KeyValueEntity newEntity = super.handleChanges(entity, object);
        final KeyValueEntity cachedEntity = hotEntities.get(object.getKey());

        if (cachedEntity != null) {
            hotEntities.replace(object.getKey(), cachedEntity, newEntity);
        }

        return newEntity;
    }

}

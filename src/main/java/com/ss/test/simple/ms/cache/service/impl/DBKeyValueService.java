package com.ss.test.simple.ms.cache.service.impl;

import com.ss.test.simple.ms.cache.SimpleMsCacheApplication;
import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import com.ss.test.simple.ms.cache.db.repository.KeyValueRepository;
import com.ss.test.simple.ms.cache.dto.KeyValueObject;
import com.ss.test.simple.ms.cache.service.KeyValueService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author JavaSaBr
 */
public class DBKeyValueService implements KeyValueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBKeyValueService.class);

    @NotNull
    private final KeyValueRepository keyValueRepository;

    @NotNull
    private final SimpleMsCacheApplication application;

    @NotNull
    private final Thread cleanThread;

    public DBKeyValueService(@NotNull final KeyValueRepository keyValueRepository,
                             @NotNull final SimpleMsCacheApplication application) {
        this.keyValueRepository = keyValueRepository;
        this.application = application;
        this.cleanThread = new Thread(this::cleanExpiredKeyValues);
        this.cleanThread.start();
    }

    private void cleanExpiredKeyValues() {
        while (true) {
            try {
                Thread.sleep(application.getCleanInterval() * 60 * 1000);
            } catch (final InterruptedException e) {
                LOGGER.warn(e.getMessage(), e);
            }
            try {
                keyValueRepository.deleteByExpiredTimeBefore(LocalDateTime.now());
            } catch (final RuntimeException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }

    @Override
    @Transactional
    public void clear() {
        keyValueRepository.deleteAll();
    }

    @Override
    public @Nullable KeyValueEntity get(@NotNull final String key) {
        return keyValueRepository.findById(key).orElse(null);
    }

    @Override
    @Transactional
    public void set(@NotNull final KeyValueObject object) {
        final Optional<KeyValueEntity> optional = keyValueRepository.findById(object.getKey());
        if (optional.isPresent()) {
            updateEntity(optional.get(), object);
        } else {
            insertEntity(object);
        }
    }

    protected void insertEntity(@NotNull final KeyValueObject object) {

        final KeyValueEntity entity = new KeyValueEntity();
        entity.setExpiredTime(getNewExpiredTime());
        entity.setValue(object.getValue());
        entity.setKey(object.getKey());

        keyValueRepository.save(entity);
    }

    protected void updateEntity(@NotNull final KeyValueEntity entity, @NotNull final KeyValueObject object) {

        final String value = entity.getValue();
        final String newValue = object.getValue();

        if (StringUtils.isEmpty(value) && StringUtils.isEmpty(newValue)) {
            handleNoChanges(entity, object);
        } else if (!StringUtils.isEmpty(value) && value.equals(newValue)) {
            handleNoChanges(entity, object);
        } else {
            handleChanges(entity, object);
        }
    }

    protected @NotNull LocalDateTime getNewExpiredTime() {
        return LocalDateTime.now().plusHours(application.getLifeTime());
    }

    protected @NotNull KeyValueEntity handleNoChanges(@NotNull final KeyValueEntity entity,
                                                      @NotNull final KeyValueObject object) {
        return entity;
    }

    protected @NotNull KeyValueEntity handleChanges(@NotNull final KeyValueEntity entity,
                                                    @NotNull final KeyValueObject object) {
        entity.setValue(object.getValue());
        entity.setExpiredTime(getNewExpiredTime());
        return keyValueRepository.save(entity);
    }
}

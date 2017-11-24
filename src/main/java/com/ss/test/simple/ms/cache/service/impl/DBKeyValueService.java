package com.ss.test.simple.ms.cache.service.impl;

import static java.time.LocalDateTime.now;
import com.ss.test.simple.ms.cache.SimpleMsCacheApplication;
import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import com.ss.test.simple.ms.cache.db.repository.KeyValueRepository;
import com.ss.test.simple.ms.cache.dto.KeyValueObject;
import com.ss.test.simple.ms.cache.service.KeyValueService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    public DBKeyValueService(@NotNull final KeyValueRepository keyValueRepository,
                             @NotNull final SimpleMsCacheApplication application) {
        this.keyValueRepository = keyValueRepository;
        this.application = application;
        final Thread cleanThread = new Thread(this::cleanExpiredKeyValues);
        cleanThread.start();
    }

    protected @NotNull SimpleMsCacheApplication getApplication() {
        return application;
    }

    private void cleanExpiredKeyValues() {
        final SimpleMsCacheApplication application = getApplication();
        while (true) {
            try {
                Thread.sleep(application.getCleanInterval() * 60 * 1000);
            } catch (final InterruptedException e) {
                LOGGER.warn(e.getMessage(), e);
            }
            try {
                clean();
            } catch (final RuntimeException e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }

    protected void clean() {
        keyValueRepository.deleteByExpiredTimeBefore(now());
    }

    @Override
    @Transactional
    public void clear() {
        keyValueRepository.deleteAll();
    }

    @Override
    @Transactional
    public @Nullable KeyValueEntity get(@NotNull final String key) {
        return keyValueRepository.findById(key).orElse(null);
    }

    @Override
    public void set(@NotNull final KeyValueObject object) {
        try {
            tryToSet(object);
        } catch (final DataIntegrityViolationException e) {
            // it seems we found a collision with the key
        } catch (final ObjectOptimisticLockingFailureException e) {
            // it seems we have newer value of this
        }
    }

    @Transactional
    public void tryToSet(@NotNull final KeyValueObject object) {
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
        final SimpleMsCacheApplication application = getApplication();
        return now().plusHours(application.getLifeTime());
    }

    public @NotNull KeyValueEntity handleNoChanges(@NotNull final KeyValueEntity entity,
                                                   @NotNull final KeyValueObject object) {
        return entity;
    }

    public @NotNull KeyValueEntity handleChanges(@NotNull final KeyValueEntity entity,
                                                 @NotNull final KeyValueObject object) {
        entity.setValue(object.getValue());
        entity.setExpiredTime(getNewExpiredTime());
        return keyValueRepository.save(entity);
    }
}

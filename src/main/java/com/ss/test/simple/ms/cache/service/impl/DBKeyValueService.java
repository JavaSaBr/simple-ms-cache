package com.ss.test.simple.ms.cache.service.impl;

import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import com.ss.test.simple.ms.cache.db.repository.KeyValueRepository;
import com.ss.test.simple.ms.cache.dto.KeyValueObject;
import com.ss.test.simple.ms.cache.service.KeyValueService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service("keyValueService")
public class DBKeyValueService implements KeyValueService {

    @NotNull
    private final KeyValueRepository keyValueRepository;

    public DBKeyValueService(@NotNull final KeyValueRepository keyValueRepository) {
        this.keyValueRepository = keyValueRepository;
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
        return LocalDateTime.now().plusHours(1);
    }

    protected void handleNoChanges(@NotNull final KeyValueEntity entity, @NotNull final KeyValueObject object) {
    }

    protected void handleChanges(@NotNull final KeyValueEntity entity, @NotNull final KeyValueObject object) {

        entity.setValue(object.getValue());
        entity.setExpiredTime(getNewExpiredTime());

        keyValueRepository.save(entity);
    }
}

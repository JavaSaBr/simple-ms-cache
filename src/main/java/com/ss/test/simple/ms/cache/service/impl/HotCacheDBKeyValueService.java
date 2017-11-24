package com.ss.test.simple.ms.cache.service.impl;

import com.ss.test.simple.ms.cache.SimpleMsCacheApplication;
import com.ss.test.simple.ms.cache.db.repository.KeyValueRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author JavaSaBr
 */
@Service("keyValueService")
public class HotCacheDBKeyValueService extends DBKeyValueService {

    @Autowired
    public HotCacheDBKeyValueService(@NotNull final KeyValueRepository keyValueRepository,
                                     @NotNull final SimpleMsCacheApplication application) {
        super(keyValueRepository, application);
    }
}

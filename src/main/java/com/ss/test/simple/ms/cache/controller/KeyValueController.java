package com.ss.test.simple.ms.cache.controller;

import static org.springframework.http.ResponseEntity.*;
import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import com.ss.test.simple.ms.cache.dto.KeyValueObject;
import com.ss.test.simple.ms.cache.service.KeyValueService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author JavaSaBr
 */
@RestController()
public class KeyValueController {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyValueController.class);

    @NotNull
    private final KeyValueService keyValueService;

    @Autowired
    public KeyValueController(@NotNull final KeyValueService keyValueService) {
        this.keyValueService = keyValueService;
    }

    @GetMapping(value = "{key}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @NotNull ResponseEntity<?> get(@PathVariable("key") @NotNull final String key) {
        final KeyValueEntity entity = keyValueService.get(key);
        if (entity == null) {
            return notFound().build();
        } else {
            return ok(new KeyValueObject(entity));
        }
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public @NotNull ResponseEntity<?> set(@RequestBody @NotNull final KeyValueObject object) {

        if (object.getKey() == null) {
            return badRequest().build();
        }

        try {
            keyValueService.set(object);
        } catch (final RuntimeException e) {
            LOGGER.warn(e.getMessage(), e);
            return status(HttpStatus.INTERNAL_SERVER_ERROR).body(e);
        }

        return ok(object);
    }
}

package com.ss.test.simple.ms.cache.controller;

import com.ss.test.simple.ms.cache.db.entity.KeyValueEntity;
import com.ss.test.simple.ms.cache.dto.KeyValueObject;
import com.ss.test.simple.ms.cache.service.KeyValueService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author JavaSaBr
 */
@RestController()
public class KeyValueController {

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
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(new KeyValueObject(entity));
        }
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public @NotNull ResponseEntity<?> set(@RequestBody @NotNull final KeyValueObject object) {

        if (object.getKey() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            keyValueService.set(object);
        } catch (final RuntimeException e) {
            return ResponseEntity.badRequest().body(e);
        }

        return ResponseEntity.ok(object);
    }
}

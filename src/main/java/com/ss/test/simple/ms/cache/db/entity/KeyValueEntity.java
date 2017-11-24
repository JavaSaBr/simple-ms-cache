package com.ss.test.simple.ms.cache.db.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The key value entity.
 *
 * @author JavaSaBr
 */
@Entity
@Table(name = "key_value")
public final class KeyValueEntity {

    @Id
    @Column(name = "key", unique = true, nullable = false)
    private String key;

    @Column(name = "value")
    private String value;

    @Column(name = "expired_time", nullable = false)
    private LocalDateTime expiredTime;

    @Version
    private long version;

    public KeyValueEntity() {
    }

    private KeyValueEntity(final String key, final String value, final LocalDateTime expiredTime, final long version) {
        this.key = key;
        this.value = value;
        this.expiredTime = expiredTime;
        this.version = version;
    }

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(final long version) {
        this.version = version;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(final LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final KeyValueEntity that = (KeyValueEntity) o;
        return getKey() != null ? getKey().equals(that.getKey()) : that.getKey() == null;
    }

    @Override
    public int hashCode() {
        return getKey() != null ? getKey().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "KeyValueEntity{" + "key='" + key + '\'' + ", value='" + value + '\'' + ", expiredTime=" + expiredTime +
                '}';
    }
}

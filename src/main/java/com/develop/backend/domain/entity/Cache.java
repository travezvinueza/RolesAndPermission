package com.develop.backend.domain.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import java.sql.Timestamp;

@Entity
@Table(name = "cache")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cache {
    @Id
    @Column(name = "key", columnDefinition = "TEXT", nullable = false)
    private String key;

    @Type(JsonType.class)
    @Column(name = "value", columnDefinition = "jsonb", nullable = false)
    private String value;

    @CreationTimestamp
    @Column(name = "created_at_utc", updatable = false)
    private Timestamp createdAtUtc;
}

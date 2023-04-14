package com.sam2n.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * Base abstract class for entities which will hold definitions for created, last modified, created by,
 * last modified by attributes.
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @CreatedBy
    @Column(nullable = false, length = 50, updatable = false)
    @JsonIgnore
    private String createdBy;
    @CreatedDate
    @Column(updatable = false)
    @JsonIgnore
    @Builder.Default
    private Instant createdDate = Instant.now();
    @LastModifiedBy
    @Column(length = 50)
    @JsonIgnore
    private String lastModifiedBy;
    @LastModifiedDate
    @Column()
    @JsonIgnore
    @Builder.Default
    private Instant lastModifiedDate = Instant.now();
}

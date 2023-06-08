package com.sam2n.backend.domain;

import com.sam2n.backend.domain.enumeration.FitnessAccountType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class FitnessAccount extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "UUID")
    @EqualsAndHashCode.Include
    @Column(nullable = false, updatable = false)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private FitnessAccountType fitnessAccountType;
    private String url;
    private Boolean isActive;
    @ManyToOne
    private User user;
}

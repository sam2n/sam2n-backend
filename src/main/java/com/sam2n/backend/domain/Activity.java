package com.sam2n.backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sam2n.backend.domain.enumeration.ActivityState;
import com.sam2n.backend.domain.enumeration.SportType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Activity extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Double distance;
    private Double avgPace;
    private Duration movingTime;
    private Integer calories;
    private Double avgHeartRate;
    @Enumerated(EnumType.STRING)
    private SportType sportType;
    private String link;
    private Instant activityDate;
    private ActivityState activityState;

    @ManyToOne
    @JsonIgnoreProperties(value = {"fitness_account"}, allowSetters = true)
    private FitnessAccount account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    @JsonIgnore
    @ToString.Exclude
    private Transaction transaction;

}

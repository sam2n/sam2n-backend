package com.sam2n.backend.domain;

import com.sam2n.backend.domain.enumeration.TransactionState;
import com.sam2n.backend.domain.enumeration.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Transaction extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    @Column(nullable = false, updatable = false)
    private UUID id;
    private Double amount;
    private String message;
    @Enumerated(EnumType.STRING)
    private TransactionState transactionState;
    private Instant dateOfPayment;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    /* In case if TransactionType:
        TRANSFORMED_ACTIVITY - id from Activity entity
        DONATION - id from MoneyRecipient entity
     */
    private UUID reasonId;
    @ManyToOne
    private Wallet wallet;
}

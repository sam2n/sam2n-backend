package com.sam2n.backend.domain;

import com.sam2n.backend.domain.enumeration.TransactionState;
import com.sam2n.backend.domain.enumeration.TransactionType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Transaction extends AbstractAuditingEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
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
    private Long reasonId;
    @ManyToOne
    private Wallet wallet;
}

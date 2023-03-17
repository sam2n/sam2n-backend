package com.sam2n.backend.domain.enumeration;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TransactionType {
    TRANSFORMED_ACTIVITY(true),
    DONATION(false);

    private final Boolean isDebit;
}

package com.sam2n.backend.repository;

import com.sam2n.backend.domain.MoneyRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyRecipientRepository extends JpaRepository<MoneyRecipient, Long> {
}

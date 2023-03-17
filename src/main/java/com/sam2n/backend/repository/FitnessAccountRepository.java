package com.sam2n.backend.repository;

import com.sam2n.backend.domain.FitnessAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FitnessAccountRepository extends JpaRepository<FitnessAccount, Long> {
}

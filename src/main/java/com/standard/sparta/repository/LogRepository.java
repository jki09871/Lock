package com.standard.sparta.repository;

import com.standard.sparta.domain.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<PaymentLog, Long> {
}

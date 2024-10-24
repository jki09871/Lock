package com.standard.sparta.transaction.service;

import com.standard.sparta.domain.PaymentLog;
import com.standard.sparta.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class TxService3 {

    private final LogRepository paymentLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processLog() {
        printActiveTransaction("processLog()");

        // 1. 저장 - PaymentLog 저장
        PaymentLog newPaymentLog = new PaymentLog();
        paymentLogRepository.save(newPaymentLog);
        throw new RuntimeException();
    }

    private void printActiveTransaction(String methodName) {
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        boolean isNewTransaction = TransactionAspectSupport.currentTransactionStatus().isNewTransaction();
        log.info("-> {}: isTxActive: {}, isNew: {}", methodName, actualTransactionActive, isNewTransaction);
    }
}

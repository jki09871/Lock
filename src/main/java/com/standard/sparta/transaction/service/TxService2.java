package com.standard.sparta.transaction.service;

import com.standard.sparta.domain.Payment;
import com.standard.sparta.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class TxService2 {

    private final PaymentRepository paymentRepository;

    @Transactional
    public void processPayment() {
        printActiveTransaction("processPayment()");

        // 1. 저장 - Payment 저장
        Payment newPayment = new Payment();
        paymentRepository.save(newPayment);
    }

    private void printActiveTransaction(String methodName) {
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        boolean isNewTransaction = TransactionAspectSupport.currentTransactionStatus().isNewTransaction();
        log.info("-> {}: isTxActive: {}, isNew: {}", methodName, actualTransactionActive, isNewTransaction);
    }
}

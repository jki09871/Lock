package com.standard.sparta.transaction.service;

import com.standard.sparta.domain.Course;
import com.standard.sparta.domain.Payment;
import com.standard.sparta.domain.PaymentLog;
import com.standard.sparta.repository.CourseRepository;
import com.standard.sparta.repository.LogRepository;
import com.standard.sparta.repository.PaymentRepository;
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
public class EnrollmentService {

    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;
    private final LogRepository logRepository;

    private final TxService1 txService1;
    private final TxService2 txService2;
    private final TxService3 txService3;

    /**
     * 문제 시나리오
     */
    @Transactional
    public void processEnrollV1() {
        printActiveTransaction("::: EnrollmentService.processEnrollV1()");

        // 1. 수강 처리: Course 엔티티 생성
        processCourse();

        // 2. 결제 처리: Payment 엔티티 생성
        processPayment();

        // 3. 로그 처리: PaymentLog 로그 생성
        try {
            processLog();
        } catch (Exception e) {
            log.info("로그 처리 예외 발생");
        }
    }

    @Transactional
    public void processCourse() {
        printActiveTransaction("processCourse()");

        // 1. 저장 - Course 저장
        Course newCourse = Course.createNewCourse("newSpring");
        courseRepository.save(newCourse);
    }

    @Transactional
    public void processPayment() {
        printActiveTransaction("processPayment()");

        // 1. 저장 - Payment 저장
        Payment newPayment = new Payment();
        paymentRepository.save(newPayment);
    }

    public void processLog() {
        printActiveTransaction("processLog()");

        // 1. 저장 - PaymentLog 저장
        PaymentLog newPaymentLog = new PaymentLog();
        logRepository.save(newPaymentLog);
        throw new RuntimeException();
    }

    /**
     * 트랜잭션 적용 여부를 확인합니다.
     */
    private void printActiveTransaction(String methodName) {
        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        boolean isNewTx = TransactionAspectSupport.currentTransactionStatus().isNewTransaction();
        log.info("{} - isActive: {}, isNew: {}", methodName, actualTransactionActive, isNewTx);
    }

    /**
     * 문제 해결
     */
    @Transactional
    public void processEnrollV2() {
        printActiveTransaction("::: processEnroll()");

        // 1. 수강 처리
        txService1.processCourse();

        // 2. 결제 처리
        txService2.processPayment();

        // 3. 로그처리
        try {
            txService3.processLog();
        } catch (Exception e) {
            log.info("::: 로그 처리 실패");
        }
    }
}

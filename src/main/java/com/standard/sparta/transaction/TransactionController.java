package com.standard.sparta.transaction;

import com.standard.sparta.transaction.service.EnrollmentService;
import com.standard.sparta.transaction.service.RollbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final RollbackService rollbackService;
    private final EnrollmentService enrollmentService;

    /**
     * 롤백 정책 확인 API
     */
    @GetMapping("/rollback")
    public String transactionRollbackAPI() throws Exception {
        rollbackService.updateCourseWithNoRollbackFor();
        return "success";
    }

    /**
     * 트랜잭션 사용시 주의 사항 1.
     * 트랜잭션 적용 안되는 문제
     */
    @GetMapping
    public String startEnrollment() {
//        enrollmentService.processEnrollV1();
        enrollmentService.processEnrollV2();
        return "success";
    }
}

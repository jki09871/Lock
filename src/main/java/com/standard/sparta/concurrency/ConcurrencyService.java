package com.standard.sparta.concurrency;

import com.standard.sparta.domain.Course;
import com.standard.sparta.repository.CourseRepository;
import com.standard.sparta.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConcurrencyService {

    private final CourseService courseService;
    private final CourseRepository courseRepository;

    /**
     * 락 없이 사용
     */
    @Transactional
    public void callWithoutLock(Long courseId) {
        courseService.updateCourseLike(courseId);
    }

    /**
     * 비관적 락 사용
     */
    @Transactional
    public void callPessimistic(Long courseId) {
        // 1. 수업 엔티티 조회
        Course foundCourse = courseRepository.findByIdWithPessimisticLock(courseId)
                .orElseThrow(RuntimeException::new);

        // 2. like 증가
        foundCourse.increaseLike();
    }

    /**
     * 낙관적 락 사용
     */
    public int callOptimistic(Long courseId) {
        boolean success = false;
        int failureCount = 0; // 실패 횟수 기록

        while (!success) {
            try {
                // 1. 로직 수행: 좋아요 증가
                courseService.updateCourseLike(courseId);

                // 2. 성공처리
                success = true;
            } catch (ObjectOptimisticLockingFailureException e) {
                failureCount++;
            }
        }
        return failureCount;
    }
}

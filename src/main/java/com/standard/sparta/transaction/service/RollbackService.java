package com.standard.sparta.transaction.service;

import com.standard.sparta.domain.Course;
import com.standard.sparta.exception.CheckedExceptionExample;
import com.standard.sparta.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RollbackService {

    private final CourseRepository courseRepository;

    /**
     * 기본 rollback 정책
     * 언체크예외(RuntimeException) 만 rollback 을 시킵니다.
     */
    @Transactional
    public void updateCourseWithDefault() throws Exception {

        // 1. 조회: init 수업 데이터 조회
        Course foundCourse = courseRepository.findById(1L)
                .orElseThrow(RuntimeException::new);

        // 2. 변경 후 저장: init -> java
        Course updateCourse1 = foundCourse.updateName("java");
        Course savedCourse1 = courseRepository.save(updateCourse1);

        if (true) {
            throw new Exception();
        }

        // 3. 변경 후 저장: java -> mysql
        Course updateCourse2 = savedCourse1.updateName("mysql");
        Course save = courseRepository.save(updateCourse2);
    }

    /**
     * rollbackFor 활용
     * 기존 rollback 정책 조건에 특정 예외를 추가시킵니다.
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCourseWithRollbackFor() throws Exception {

        // 1. 조회: init 수업 데이터 조회
        Course foundCourse = courseRepository.findById(1L)
                .orElseThrow(RuntimeException::new);

        // 2. 변경 후 저장: init -> java
        Course updateCourse1 = foundCourse.updateName("java");
        Course savedCourse1 = courseRepository.save(updateCourse1);

        if (true) {
            throw new Exception();
        }

        // 3. 변경 후 저장: java -> mysql
        Course updateCourse2 = savedCourse1.updateName("mysql");
        Course save = courseRepository.save(updateCourse2);
    }

    /**
     * noRollbackFor 활용
     * 기존 rollback 정책 조건에 특정 예외를 제거 시킵니다.
     */
    @Transactional(noRollbackFor = RuntimeException.class)
    public void updateCourseWithNoRollbackFor() {

        // 1. 조회: init 수업 데이터 조회
        Course foundCourse = courseRepository.findById(1L)
                .orElseThrow(RuntimeException::new);

        // 2. 변경 후 저장: init -> java
        Course updateCourse1 = foundCourse.updateName("java");
        Course savedCourse1 = courseRepository.save(updateCourse1);

        if (true) {
            throw new RuntimeException();
        }

        // 3. 변경 후 저장: java -> mysql
        Course updateCourse2 = savedCourse1.updateName("mysql");
        Course save = courseRepository.save(updateCourse2);
    }
}
















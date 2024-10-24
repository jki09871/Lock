package com.standard.sparta.concurrency;

import com.standard.sparta.domain.Course;
import com.standard.sparta.repository.CourseRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 동시성 테스트
 */
@SpringBootTest
public class ConcurrencyServiceTest {

    @Autowired
    private ConcurrencyService concurrencyService;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * 락없이 테스트
     * 테스트시 Course 엔티티에서 Version 주석 필요
     */
    @Test
    @DisplayName("락없이 테스트")
    public void testWithoutLock() throws InterruptedException {
        // given
        Course newCourse = Course.createNewCourse("noLockCourse");
        Course savedCourse = courseRepository.save(newCourse);

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                try {
                    concurrencyService.callWithoutLock(savedCourse.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        Course updatedCourse = courseRepository.findById(savedCourse.getId()).get();
        System.out.println("Final like count (no lock): " + updatedCourse.getLikeCnt());
    }

    /**
     * 비관락 사용
     */
    @Test
    @DisplayName("비관락 테스트")
    public void testPessimistic() throws InterruptedException {
        // given
        Course newCourse = Course.createNewCourse("pessimisticCourse");
        Course savedCourse = courseRepository.save(newCourse);

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                try {
                    concurrencyService.callPessimistic(savedCourse.getId());
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        Course updatedCourse = courseRepository.findById(savedCourse.getId()).get();
        System.out.println("Final like count (pessimistic): " + updatedCourse.getLikeCnt());
    }

    /**
     * 낙관락 테스트
     * 테스트시 Course 엔티티에서 Version 주석 활성화 필요.
     */
    @Test
    @DisplayName("낙관락 테스트")
    public void testOptimistic() throws InterruptedException {
        // given
        Course newCourse = Course.createNewCourse("optimistic");
        Course savedCourse = courseRepository.save(newCourse);
        AtomicInteger totalFailures = new AtomicInteger(0);

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                try {
                    int attempts = concurrencyService.callOptimistic(savedCourse.getId());
                    totalFailures.addAndGet(attempts);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        // then
        Course updatedCourse = courseRepository.findById(savedCourse.getId()).get();
        System.out.println("Total attempts: " + totalFailures.get());
        System.out.println("Final like count (optimistic): " + updatedCourse.getLikeCnt());
    }
}

package com.standard.sparta.concurrency;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concurrency")
public class ConcurrencyController {

    private final ConcurrencyService concurrencyService;

    /**
     * 락없이 사용
     */
    @GetMapping
    public String callAPI() {
        concurrencyService.callWithoutLock(1L);
        return "success";
    }

    /**
     * 비관락
     */
    @GetMapping("/pessimistic")
    public String pessimisticAPI() {
        concurrencyService.callPessimistic(1L);
        return "success";
    }

    /**
     * 낙관락
     */
    @GetMapping("/optimistic")
    public String optimisticAPI() {
        concurrencyService.callOptimistic(1L);
        return "success";
    }

}

package com.ozon.online.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class RequestLimitAspect {

    private static final int MAX_REQUESTS = 100;
    private static final long INTERVAL = 60000;

    private AtomicInteger requestCount = new AtomicInteger(0);
    private long lastRequestTime = System.currentTimeMillis();

    @Before("execution(* com.ozon.online.controller.*.*(..))")
    public void limitRequests() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRequestTime > INTERVAL) {
            requestCount.set(0);
            lastRequestTime = currentTime;
        }
        if (requestCount.incrementAndGet() > MAX_REQUESTS) {
            throw new RuntimeException("Превышено максимальное количество запросов в интервале");
        }
    }
}

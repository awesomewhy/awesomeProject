package com.ozon.online;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;
import scala.Some;


@SpringBootTest
class OzonAplicationTests {

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    void contextLoads() {

    }

}

package com.bhanna.security

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SecurityApplicationTests {

    @Test
    fun contextLoads() {
        val v = true
        assert(v == true, { "Test" })
    }

}

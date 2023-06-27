package com.bhanna.security.demo

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/demo")
class DemoController {

    @GetMapping("/greetings")
    fun greetings(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello from the API")
    }
}
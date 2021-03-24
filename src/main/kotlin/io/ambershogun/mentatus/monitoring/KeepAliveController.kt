package io.ambershogun.mentatus.monitoring

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class KeepAliveController {
    @GetMapping("/")
    fun keepAlive(): ResponseEntity<Any> {
        return ResponseEntity.ok().body("I'm alive!")
    }
}
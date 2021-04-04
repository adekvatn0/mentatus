package io.ambershogun.mentatus.push.controller

import io.ambershogun.mentatus.push.dto.BroadcastPush
import io.ambershogun.mentatus.push.dto.DirectPush
import io.ambershogun.mentatus.push.dto.PushResult
import io.ambershogun.mentatus.push.service.PushService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class PushController(
        private val pushService: PushService
) {

    @PostMapping("/push/broadcast")
    fun sendBroadcastPush(@Valid @RequestBody broadcastPush: BroadcastPush): PushResult {
        return pushService.sendBroadcastPush(broadcastPush)
    }

    @PostMapping("/push/direct")
    fun sendDirectPush(@Valid @RequestBody directPush: DirectPush): PushResult {
        return pushService.sendDirectPush(directPush)
    }
}
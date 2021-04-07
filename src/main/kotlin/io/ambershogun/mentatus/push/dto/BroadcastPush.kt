package io.ambershogun.mentatus.push.dto

import javax.validation.constraints.NotBlank

class BroadcastPush {
    @get:NotBlank
    var text: String? = null
}
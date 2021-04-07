package io.ambershogun.mentatus.push.dto

import javax.validation.constraints.NotBlank

class DirectPush {
    @get:NotBlank
    var text: String? = null
    @get:NotBlank
    var username: String? = null
}
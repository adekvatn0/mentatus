package io.ambershogun.mentatus.push.exception

class UserNotFoundException(
        val username: String
) : RuntimeException() {
}
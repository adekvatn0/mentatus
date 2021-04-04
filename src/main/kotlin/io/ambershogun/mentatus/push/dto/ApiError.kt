package io.ambershogun.mentatus.push.dto

data class ApiError(
        val status: String,
        val errors: List<String>
)
package io.ambershogun.mentatus.push.controller

import io.ambershogun.mentatus.push.dto.ApiError
import io.ambershogun.mentatus.push.exception.UserNotFoundException
import org.springframework.context.MessageSource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception
import java.util.*

@ControllerAdvice
class PushControllerExceptionHandler(
        private val messageSource: MessageSource
) : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [UserNotFoundException::class])
    fun handleUserNotFound(e: UserNotFoundException, request: WebRequest): ResponseEntity<Any> {
        val error = messageSource.getMessage("user.not.found", arrayOf(e.username), Locale.forLanguageTag("ru"))
        val apiError = ApiError(
                HttpStatus.BAD_REQUEST.name,
                listOf(error)
        )
        return handleExceptionInternal(e, apiError, HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request)
    }

    override fun handleMethodArgumentNotValid(
            ex: MethodArgumentNotValidException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        val errors = ArrayList<String>()
        for (error in ex.bindingResult.fieldErrors) {
            errors.add(error.field + ": " + error.defaultMessage)
        }
        for (error in ex.bindingResult.globalErrors) {
            errors.add(error.objectName + ": " + error.defaultMessage)
        }

        val apiError = ApiError(
                status.name,
                errors
        )

        return handleExceptionInternal(ex, apiError, headers, status, request)
    }
}
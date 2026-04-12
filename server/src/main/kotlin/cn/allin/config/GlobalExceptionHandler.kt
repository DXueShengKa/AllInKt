package cn.allin.config

import cn.allin.VoValidatorMessage
import jakarta.validation.ConstraintViolationException
import org.springframework.context.MessageSource
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class GlobalExceptionHandler(
    private val messageSource: MessageSource,
) {
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleValidationExceptions(
        ex: WebExchangeBindException,
        request: WebRequest,
    ): ResponseEntity<VoValidatorMessage> {
        val locale = request.locale
        val errors =
            ex.bindingResult.allErrors
                .asSequence()
                .mapNotNull {
                    it as FieldError
                    VoValidatorMessage(
                        code = it.code ?: return@mapNotNull null,
                        message = messageSource.getMessage(it, locale),
                        field = it.field,
                    )
                }.first()
        return ResponseEntity(errors, ex.statusCode)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(
        ex: ConstraintViolationException,
        request: WebRequest,
    ): ResponseEntity<VoValidatorMessage> {
        val locale = request.locale
        val errorMessage = messageSource.getMessage("error.validation.constraintViolation", null, locale)
        val error = VoValidatorMessage("validation", "constraintViolation", errorMessage)
        return ResponseEntity(error, org.springframework.http.HttpStatus.BAD_REQUEST)
    }
}

package cn.allin.config

import cn.allin.VoValidatorMessage
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.support.WebExchangeBindException

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleValidationExceptions(ex: WebExchangeBindException): ResponseEntity<VoValidatorMessage> {
        val errors =
            ex.bindingResult.allErrors
                .asSequence()
                .mapNotNull {
                    it as FieldError
                    VoValidatorMessage(it.field, it.code ?: return@mapNotNull null, it.defaultMessage ?: return@mapNotNull null)
                }.first()
        return ResponseEntity(errors, ex.statusCode)
    }
}

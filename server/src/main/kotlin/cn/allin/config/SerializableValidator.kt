package cn.allin.config

import cn.allin.VoValidator
import cn.allin.VoValidatorMessage
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializerOrNull
import org.springframework.validation.Errors
import org.springframework.validation.SmartValidator

@OptIn(InternalSerializationApi::class)
class SerializableValidator : SmartValidator {
    override fun supports(clazz: Class<*>): Boolean = clazz.kotlin.serializerOrNull() != null

    override fun validate(
        target: Any,
        errors: Errors,
    ) {
        VoValidatorMessage.validator(target)?.also {
            errors.rejectValue(it.field, it.code, it.message)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun validate(
        target: Any,
        errors: Errors,
        vararg validationHints: Any,
    ) {
        validationHints.forEach { hint ->
            if (hint is Class<*>) {
                val v = hint.constructors?.firstOrNull()?.newInstance() as? VoValidator<Any>
                v?.validator(target)?.also { it ->
                    errors.rejectValue(it.field, it.code, it.message)
                }
            }
        }
    }
}

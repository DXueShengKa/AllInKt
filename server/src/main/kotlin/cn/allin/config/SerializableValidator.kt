package cn.allin.config

import cn.allin.VoValidator
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializerOrNull
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@OptIn(InternalSerializationApi::class)
class SerializableValidator : Validator {

    override fun supports(clazz: Class<*>): Boolean {
        return clazz.kotlin.serializerOrNull() != null
    }


    override fun validate(target: Any, errors: Errors) {
        VoValidator.validator(target)?.also {
            errors.rejectValue(it.field, it.code, it.message)
        }
    }
}
package com.ip13.main.security.model.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PasswordValidator::class])
@MustBeDocumented
annotation class Password(
    val message: String = "password is too easy",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

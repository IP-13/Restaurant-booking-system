package com.ip13.main.security.model.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordValidator : ConstraintValidator<Password, String> {
    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean {
        if (value != null) {
            val isValidLength = value.length in 5..10
            val containsLowerCaseLetter = value.contains(Regex("[a-z]"))
            val containsUpperCaseLetter = value.contains(Regex("[A-Z]"))
            val containsDigit = value.contains(Regex("\\d"))
            val containsSpecialSymbol = value.contains(Regex("\\W"))
            val doesNotContainsWhitespaces = value.contains(Regex("\\S"))
            if (isValidLength &&
                containsLowerCaseLetter &&
                containsUpperCaseLetter &&
                containsDigit &&
                containsSpecialSymbol &&
                doesNotContainsWhitespaces
            ) {
                return true
            }
        }

        return false
    }
}
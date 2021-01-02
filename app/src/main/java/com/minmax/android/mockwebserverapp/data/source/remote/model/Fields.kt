package com.minmax.android.mockwebserverapp.data.source.remote.model

/**
 * Created by murodjon on 2021/01/02
 */
sealed class Fields {
    data class Email(val error: FormErrors) : Fields()
    data class Password(val error: FormErrors) : Fields()
    data class ConfirmPassword(val error: FormErrors) : Fields()
    data class FullName(val error: FormErrors) : Fields()
}

enum class FormErrors {
    MISSING_VALUE,
    INVALID_EMAIL,
    PASSWORD_TO_SHORT,
    PASSWORD_NOT_MATCHING,
    INVALID_PASSWORD
}
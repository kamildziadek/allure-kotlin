package io.qameta.allure.kotlin

@Deprecated("scheduled to remove in 3.0")
data class ReadError(val exception: Throwable, val message: String) {

    constructor(exception: Throwable, message: String, vararg args: Any)
            : this(exception, if (args.isNotEmpty()) String.format(message, *args) else message)

}
package com.cap.plugins.common.util

fun String.isLowerCase(): Boolean {
    return this.asSequence().all { !it.isLetter() || it.isLowerCase() }
}

fun String.isUpperCase(): Boolean {
    return this.asSequence().all { !it.isLetter() || it.isUpperCase() }
}

fun String.isNotLowerCase(): Boolean {
    return !isLowerCase()
}

fun String.isNotUpperCase(): Boolean {
    return !isLowerCase()
}

fun String.isCommaServerHost(): Boolean {
    return """([a-zA-Z0-9\.\-_]+:[0-9]{1,5},)*([a-zA-Z0-9-_]+\.)*([a-zA-Z0-9-_])+:[0-9]{1,5}""".toRegex().matches(this)
}

fun String.isSemicolonServerHost(): Boolean {
    return """([a-zA-Z0-9\.\-_]+:[0-9]{1,5};)*([a-zA-Z0-9-_]+\.)*([a-zA-Z0-9-_])+:[0-9]{1,5}""".toRegex().matches(this)
}
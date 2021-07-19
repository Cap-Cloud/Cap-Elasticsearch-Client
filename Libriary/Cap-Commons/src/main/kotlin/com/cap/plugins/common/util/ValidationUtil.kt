package com.cap.plugins.common.util

import com.intellij.openapi.ui.InputValidator
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.swing.InputVerifier
import javax.swing.JComponent
import javax.swing.JTextField

/**
 * 整数校验
 */
val INT_VERIFIER = object : InputVerifier() {
    override fun verify(input: JComponent): Boolean {
        // 允许为空
        if ((input as JTextField).text == "") {
            return true
        }
        try {
            // 判断是否是正整数
            with(Integer.parseInt(input.text)) {
                return this <= Int.MAX_VALUE && this >= 0
            }
        } catch (e: NumberFormatException) {
            return false
        }
    }
}

/**
 * 正整数校验
 */
val INT_NON_ZERO_VERIFIER = object : InputVerifier() {
    override fun verify(input: JComponent): Boolean {
        try {
            with(Integer.parseInt((input as JTextField).text)) {
                return this <= Int.MAX_VALUE && this > 0
            }
        } catch (e: NumberFormatException) {
            return false
        }
    }
}

/**
 * Long校验
 */
val LONG_VERIFIER = object : InputVerifier() {
    override fun verify(input: JComponent) = LONG_VALIDATOR.canClose((input as JTextField).text)
}

val DATE_FORMAT = SimpleDateFormat("dd/MM/yy hh:mm:ss")

val DATE_VERIFIER = object : InputVerifier() {
    override fun verify(input: JComponent) =
        try {
            DATE_FORMAT.parse((input as JTextField).text)
            true
        } catch (e: ParseException) {
            false
        }
}

/**
 * 地址校验
 */
val SERVER_HOST_COMMA_VERIFIER = object : InputVerifier() {
    override fun verify(input: JComponent): Boolean {
        // 允许为空
        if ((input as JTextField).text == "") {
            return true
        }
        // 判断是否是地址
        return input.text.isCommaServerHost()
    }
}

/**
 * 地址校验
 */
val SERVER_HOST_SEMICOLON_VERIFIER = object : InputVerifier() {
    override fun verify(input: JComponent): Boolean {
        // 允许为空
        if ((input as JTextField).text == "") {
            return true
        }
        // 判断是否是地址
        return input.text.isSemicolonServerHost()
    }
}

/**
 * Topic校验
 */
val TOPIC_VERIFIER = object : InputVerifier() {
    override fun verify(input: JComponent) = Regex("""[a-zA-Z0-9.\-_]+""").matches((input as JTextField).text)
}


/**
 * Int类型校验
 */
val INT_VALIDATOR = object : InputValidator {
    override fun checkInput(inputString: String?) = true
    override fun canClose(inputString: String?) =
        try {
            with(Integer.parseInt(inputString)) {
                this <= Int.MAX_VALUE && this >= 0
            }
        } catch (e: NumberFormatException) {
            false
        }
}

/**
 * Long类型校验
 */
val LONG_VALIDATOR = object : InputValidator {
    override fun checkInput(inputString: String?) = true
    override fun canClose(inputString: String?) =
        try {
            with(java.lang.Long.parseLong(inputString)) {
                this <= Long.MAX_VALUE && this >= 0
            }
        } catch (e: NumberFormatException) {
            false
        }
}

/**
 * IDENTIFIER类型校验
 */
val IDENTIFIER_VALIDATOR = object : InputValidator {
    override fun checkInput(inputString: String?) = true
    override fun canClose(inputString: String?) =
        try {
            Regex("""[a-zA-Z0-9.\-_]+""").matches(inputString!!)
        } catch (e: NumberFormatException) {
            false
        }
}

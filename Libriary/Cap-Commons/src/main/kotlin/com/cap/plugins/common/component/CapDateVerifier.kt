package com.cap.plugins.common.component

import java.text.ParseException
import java.text.SimpleDateFormat
import javax.swing.InputVerifier
import javax.swing.JComponent
import javax.swing.JTextField

class CapDateVerifier(val pattern: String) : InputVerifier() {
    override fun verify(input: JComponent) =
        try {
            SimpleDateFormat(pattern).parse((input as JTextField).text)
            true
        } catch (e: ParseException) {
            false
        }
}
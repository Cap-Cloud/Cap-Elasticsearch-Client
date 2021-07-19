package com.cap.language.grammar.kit.composer

import com.squareup.kotlinpoet.ClassName
import java.io.File

internal data class Outputs(
    val outputFile: File,
    val psiPackage: String,
    val parserClass: ClassName,
    val outputPackage: String,
    val outputDirectory: File
)

internal fun getOutputs(
    outputDirectory: File,
    bnf: File,
    root: File
): Outputs {
    val outputPackage = bnf.parentFile.toRelativeString(root).replace(File.separatorChar, '.')
    fun outputDirectory(): File = File(outputDirectory, outputPackage.replace('.', File.separatorChar))
    val outputBnf = File(outputDirectory().path, bnf.name)

    return Outputs(
        outputFile = outputBnf,
        psiPackage = "${outputPackage}.psi",
        outputPackage = outputPackage,
        parserClass = ClassName(outputPackage, "${bnf.nameWithoutExtension.capitalize()}Parser"),
        outputDirectory = outputDirectory()
    )
}

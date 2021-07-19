package com.cap.language.grammar.kit.composer

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.grammarkit.GrammarKit
import org.jetbrains.grammarkit.tasks.GenerateParser
import java.io.File
import java.lang.IllegalStateException

open class GrammarKitComposerPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(GrammarKit::class.java)
        project.file("src${File.separatorChar}main${File.separatorChar}kotlin").forBnfFiles { bnfFile ->
            val outputDirectory = project.file("gen")
            val rootDir = project.file("src${File.separatorChar}main${File.separatorChar}kotlin")
            // val name = bnfFile.toRelativeString(rootDir).replace(File.separatorChar, '_').dropLast(4)
            val name = bnfFile.name.dropLast(4)

            val compose = project.tasks.register("generate${name}ComposableGrammar", BnfExtenderTask::class.java) {
                it.source(bnfFile)
                it.outputDirectory = outputDirectory
                it.include("**${File.separatorChar}*.bnf")
                it.group = "grammar"
                it.description = "Generate composable grammars from .bnf files."
            }

            val gen = project.tasks.create("generate${name}Parser", GenerateParser::class.java) { generateParserTask ->
                val outputs = getOutputs(
                    bnf = bnfFile,
                    outputDirectory = outputDirectory,
                    root = rootDir
                )

                generateParserTask.dependsOn(compose)
                generateParserTask.source = outputs.outputFile
                generateParserTask.targetRoot = outputDirectory
                generateParserTask.pathToParser = outputs.parserClass.toString().replace('.', File.separatorChar)
                generateParserTask.pathToPsiRoot = outputs.psiPackage.replace('.', File.separatorChar)
                generateParserTask.purgeOldFiles = true
                generateParserTask.group = "grammar"
            }

            project.tasks.named("compileKotlin").configure {
                it.dependsOn(gen)
            }
        }
    }

    private fun File.forBnfFiles(action: (bnfFile: File) -> Unit) {
        listFiles()?.forEach {
            if (it.isDirectory) it.forBnfFiles(action)
            if (it.extension == "bnf") action(it)
        }
    }
}
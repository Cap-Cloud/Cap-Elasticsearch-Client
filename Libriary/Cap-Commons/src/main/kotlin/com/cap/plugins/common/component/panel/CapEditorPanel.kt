package com.cap.plugins.common.component.panel

import com.intellij.json.JsonFileType
import com.intellij.lang.Language
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.ex.util.LexerEditorHighlighter
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.testFramework.LightVirtualFile
import com.intellij.ui.JBColor
import com.intellij.util.ui.JBUI
import org.jetbrains.annotations.Nullable
import java.awt.BorderLayout
import javax.swing.JPanel

open class CapEditorPanel : JPanel, Disposable {

    val project: Project
    val file: VirtualFile
    val highlighter: LexerEditorHighlighter?

    val editor: EditorEx
    var editorDocument: Document
    var psiFile: PsiFile

    constructor(project: Project, file: VirtualFile, highlighter: LexerEditorHighlighter? = null) : super(
        BorderLayout()
    ) {
        this.project = project
        this.file = file
        this.highlighter = highlighter

        editorDocument = FileDocumentManager.getInstance().getDocument(file)!!
        psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editorDocument)!!
        editor = createEditor()

        border = JBUI.Borders.customLine(JBColor.border(), 0, 0, 1, 0)
        add(editor.component, BorderLayout.CENTER)
    }

    private fun createEditor(): EditorEx {
        val editor = EditorFactory.getInstance().createEditor(editorDocument, project) as EditorEx
        editor.settings.isLineMarkerAreaShown = true
        editor.settings.isLineNumbersShown = true
        editor.settings.isRightMarginShown = true
        highlighter?.let { editor.highlighter = highlighter }
        return editor
    }

    fun setText(text: String) {
        ApplicationManager.getApplication().runWriteAction {
            editorDocument.setText(text)
        }
    }

    @Nullable
    fun getText(): String {
        return editorDocument.text
    }

    @Nullable
    fun getSelectedText(): String? {
        return editor.selectionModel.selectedText
    }

    override fun dispose() {
        EditorFactory.getInstance().releaseEditor(editor)
    }
}

open class CapJsonEditorPanel : CapEditorPanel {

    constructor(project: Project) : super(project, LightVirtualFile("result.json", JsonFileType.INSTANCE, ""), null) {
        val language = Language.findLanguageByID("JSON")!!
        val highlighter = LexerEditorHighlighter(
            SyntaxHighlighterFactory.getSyntaxHighlighter(language, null, null),
            editor.colorsScheme
        )
        editor.highlighter = highlighter
    }

}

open class CapTextEditorPanel(
    project: Project
) : CapEditorPanel(project, LightVirtualFile("result.txt", PlainTextFileType.INSTANCE, ""), null) {

}
package com.cap.plugins.elasticsearch.ui.editor.panel

import com.cap.plugins.common.component.panel.CapTextEditorPanel
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.CodeStyleManager

class ElasticsearchTextPanel(
    project: Project
) : CapTextEditorPanel(project) {

    fun updateEditorText(responseContext: ResponseContext) {
        editorDocument.setText(responseContext.response.content)
        PsiDocumentManager.getInstance(project).commitDocument(editorDocument)
        editor.caretModel.moveToOffset(0)
        CodeStyleManager.getInstance(project).reformatText(psiFile, 0, psiFile.textRange.endOffset)
    }

}
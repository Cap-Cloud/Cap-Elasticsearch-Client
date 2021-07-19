package com.cap.plugins.elasticsearch.ui.editor.panel

import com.cap.plugins.common.component.panel.CapJsonEditorPanel
import com.cap.plugins.common.util.ObjectMapper
import com.cap.plugins.elasticsearch.ui.editor.model.ResponseContext
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.codeStyle.CodeStyleManager

class ElasticsearchJsonPanel(
    project: Project
) : CapJsonEditorPanel(project) {

    fun updateEditorText(responseContext: ResponseContext) {
        val search = responseContext.request.path.contains("/_search")
        val context = ObjectMapper.jsonMapper.readTree(responseContext.response.content)
        if (search && context.has("hits")) {
            editorDocument.setText(ObjectMapper.jsonMapper.writeValueAsString(context.get("hits").get("hits")))
        } else {
            editorDocument.setText(responseContext.response.content)
        }
        PsiDocumentManager.getInstance(project).commitDocument(editorDocument)
        editor.caretModel.moveToOffset(0)
        CodeStyleManager.getInstance(project).reformatText(psiFile, 0, psiFile.textRange.endOffset)
    }

}
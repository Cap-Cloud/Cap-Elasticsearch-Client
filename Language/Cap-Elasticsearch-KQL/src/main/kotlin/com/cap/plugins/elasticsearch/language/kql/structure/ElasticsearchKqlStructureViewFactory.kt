package com.cap.plugins.elasticsearch.language.kql.structure

import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.ide.structureView.StructureViewModel
import com.intellij.ide.structureView.StructureViewModelBase
import com.intellij.ide.structureView.TreeBasedStructureViewBuilder
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class ElasticsearchKqlStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
        return object : TreeBasedStructureViewBuilder() {
            override fun isRootNodeShown(): Boolean {
                return false
            }

            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                val root = ElasticsearchKqlStructureViewElement.create(psiFile, null, null);
                return StructureViewModelBase(psiFile, editor, root)
            }
        }
    }
}
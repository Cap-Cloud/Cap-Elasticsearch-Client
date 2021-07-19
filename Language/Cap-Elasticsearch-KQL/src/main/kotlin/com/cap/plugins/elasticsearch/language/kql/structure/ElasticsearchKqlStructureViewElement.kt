package com.cap.plugins.elasticsearch.language.kql.structure

import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlFile
import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.structureView.impl.common.PsiTreeElementBase
import com.intellij.navigation.ColoredItemPresentation
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import javax.swing.Icon

class ElasticsearchKqlStructureViewElement : PsiTreeElementBase<PsiElement>, ColoredItemPresentation {
    private var myPresentationText: String

    private var myLocation: String?

    private var myIcon: Icon?

    private var myIsValid: Boolean

    private constructor(
        element: PsiElement,
        text: String,
        location: String?,
        icon: Icon?,
        isValid: Boolean
    ) : super(element) {
        myPresentationText = text
        myLocation = location
        myIcon = icon
        myIsValid = isValid
    }

    override fun getChildrenBase(): MutableCollection<StructureViewTreeElement> {
        if (element is ElasticsearchKqlFile){

        }
       return mutableListOf()
    }

    override fun getTextAttributesKey(): TextAttributesKey? {
        if (!this.myIsValid) {
            return CodeInsightColors.ERRORS_ATTRIBUTES
        }
        return null
    }

    override fun getPresentableText(): String {
        return this.myPresentationText
    }

    companion object {
        fun create(
            element: PsiElement,
            text: String?,
            icon: Icon?
        ): StructureViewTreeElement {
            return ElasticsearchKqlStructureViewElement(
                element,
                text ?: "<not defined>",
                null,
                icon,
                !text.isNullOrEmpty()
            )
        }
    }
}
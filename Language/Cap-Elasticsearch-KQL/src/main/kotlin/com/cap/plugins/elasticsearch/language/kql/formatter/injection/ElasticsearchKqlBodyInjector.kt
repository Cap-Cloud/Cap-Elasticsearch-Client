package com.cap.plugins.elasticsearch.language.kql.formatter.injection

import com.cap.plugins.common.annotation.Keep
import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlCompositeElement
import com.cap.plugins.elasticsearch.language.kql.grammar.psi.ElasticsearchKqlRequestBody
import com.intellij.lang.Language
import com.intellij.lang.injection.MultiHostInjector
import com.intellij.lang.injection.MultiHostRegistrar
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiLanguageInjectionHost
import com.intellij.util.containers.ContainerUtil

@Keep
class ElasticsearchKqlBodyInjector : MultiHostInjector {

    private val injectionContext: MutableList<Class<out ElasticsearchKqlCompositeElement>> =
        mutableListOf(ElasticsearchKqlRequestBody::class.java)

    override fun elementsToInjectIn(): MutableList<out Class<out PsiElement>> {
        return injectionContext
    }

    override fun getLanguagesToInject(registrar: MultiHostRegistrar, context: PsiElement) {
        if (context is ElasticsearchKqlRequestBody && context.jsonObjectList.size < 2) {
            context.jsonObjectList.forEach { requestBody ->
                val language =
                    ContainerUtil.getFirstItem(Language.findInstancesByMimeType("application/json")) as Language
                val injector = registrar.startInjecting(language)
                val startOffset = 0
                val endOffset = requestBody.textLength
                injector.addPlace(
                    null,
                    null,
                    requestBody as PsiLanguageInjectionHost,
                    TextRange.create(startOffset, endOffset)
                )
                injector.doneInjecting()
            }
        }
    }
}
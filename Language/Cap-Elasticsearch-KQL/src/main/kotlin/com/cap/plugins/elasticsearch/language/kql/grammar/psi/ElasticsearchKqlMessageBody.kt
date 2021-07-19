package com.cap.plugins.elasticsearch.language.kql.grammar.psi

import com.intellij.psi.PsiLanguageInjectionHost

interface ElasticsearchKqlMessageBody : ElasticsearchKqlJsonObject, PsiLanguageInjectionHost {
}
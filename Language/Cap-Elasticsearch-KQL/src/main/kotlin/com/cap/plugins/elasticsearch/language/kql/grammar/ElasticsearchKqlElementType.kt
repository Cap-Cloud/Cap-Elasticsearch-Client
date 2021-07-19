package com.cap.plugins.elasticsearch.language.kql.grammar

import com.cap.plugins.elasticsearch.language.kql.ElasticsearchKqlLanguage
import com.intellij.psi.tree.IElementType

class ElasticsearchKqlElementType(name: String) : IElementType(name, ElasticsearchKqlLanguage.INSTANCE)
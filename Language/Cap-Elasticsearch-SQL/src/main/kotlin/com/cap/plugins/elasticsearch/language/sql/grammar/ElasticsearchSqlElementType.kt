package com.cap.plugins.elasticsearch.language.sql.grammar

import com.cap.plugins.elasticsearch.language.sql.ElasticsearchSqlLanguage
import com.intellij.psi.tree.IElementType

class ElasticsearchSqlElementType(name: String) : IElementType(name, ElasticsearchSqlLanguage.INSTANCE)
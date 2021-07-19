package com.cap.plugins.common.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.core.util.Separators
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

object ObjectMapper {
    val jsonMapper: ObjectMapper = jacksonObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
        .setDefaultPrettyPrinter(CapPrettyPrinter())
        .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)

    val xmlMapper: ObjectMapper = XmlMapper()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
}

class CapPrettyPrinter : DefaultPrettyPrinter {

    constructor() {
        _arrayIndenter = DEFAULT_INDENTER
        _objectIndenter = DEFAULT_INDENTER
    }

    constructor(base: DefaultPrettyPrinter) : super(base)

    override fun createInstance(): CapPrettyPrinter {
        check(javaClass == CapPrettyPrinter::class.java) {
            ("Failed `createInstance()`: " + javaClass.name
                    + " does not override method; it has to")
        }
        return CapPrettyPrinter(this)
    }

    override fun withSeparators(separators: Separators): CapPrettyPrinter {
        _separators = separators
        _objectFieldValueSeparatorWithSpaces = separators.objectFieldValueSeparator.toString() + " "
        return this
    }

    override fun writeEndArray(g: JsonGenerator, nrOfValues: Int) {
        if (!_arrayIndenter.isInline) {
            --_nesting
        }
        if (nrOfValues > 0) {
            _arrayIndenter.writeIndentation(g, _nesting)
        }
        g.writeRaw(']')
    }

    override fun writeEndObject(g: JsonGenerator, nrOfEntries: Int) {
        if (!_objectIndenter.isInline) {
            --_nesting
        }
        if (nrOfEntries > 0) {
            _objectIndenter.writeIndentation(g, _nesting)
        }
        g.writeRaw('}')
    }

    companion object {
        val DEFAULT_INDENTER = DefaultIndenter("  ", "\n")
    }
}
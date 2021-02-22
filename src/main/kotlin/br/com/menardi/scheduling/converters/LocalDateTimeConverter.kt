package br.com.menardi.scheduling.converters

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.KlaxonException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter : Converter {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    override fun canConvert(cls: Class<*>): Boolean {
        return cls == LocalDateTime::class.java
    }

    override fun fromJson(jv: JsonValue): LocalDateTime {
        if (jv.string == null) {
            throw KlaxonException("Couldn't parse LocalDateTime: ${jv.string}")
        }

        return LocalDateTime.parse(jv.string, dateTimeFormatter)
    }

    override fun toJson(value: Any): String {
        val localDateTime = value as LocalDateTime
        return """"${localDateTime.format(dateTimeFormatter)}""""
    }
}
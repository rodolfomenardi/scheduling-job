package br.com.menardi.scheduling.service

import br.com.menardi.scheduling.annotations.JsonDuration
import br.com.menardi.scheduling.annotations.JsonLocalDateTime
import br.com.menardi.scheduling.converters.DurationConverter
import br.com.menardi.scheduling.converters.LocalDateTimeConverter
import com.beust.klaxon.Klaxon
import com.beust.klaxon.KlaxonException
import java.io.Reader

class JsonParserService {
    inline fun <reified T> parseArrayFromReader(reader: Reader): List<T> =
        Klaxon()
            .fieldConverter(JsonDuration::class, DurationConverter())
            .fieldConverter(JsonLocalDateTime::class, LocalDateTimeConverter())
            .parseArray(reader) ?: emptyList()

    inline fun <reified T> parseFromReader(reader: Reader): T =
        Klaxon()
            .fieldConverter(JsonLocalDateTime::class, LocalDateTimeConverter())
            .parse<T>(reader) ?: throw KlaxonException("Impossível fazer o parse do json")

    inline fun <reified T> toJsonString(jsonObject: T): String =
        Klaxon()
            .fieldConverter(JsonDuration::class, DurationConverter())
            .fieldConverter(JsonLocalDateTime::class, LocalDateTimeConverter())
            .toJsonString(jsonObject)
}
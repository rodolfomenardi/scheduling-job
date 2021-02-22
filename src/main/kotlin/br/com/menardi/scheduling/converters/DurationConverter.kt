package br.com.menardi.scheduling.converters

import br.com.menardi.scheduling.extensions.toDisplay
import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.KlaxonException
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class DurationConverter : Converter {
    override fun canConvert(cls: Class<*>): Boolean {
        return cls == Duration::class.java
    }

    override fun fromJson(jv: JsonValue): Duration {
        if (jv.string == null) {
            throw KlaxonException("Couldn't parse Duration: ${jv.string}")
        }

        val localTime = LocalTime.parse(jv.string, DateTimeFormatter.ofPattern("HH:mm:ss"))
        return Duration.between(LocalTime.of(0, 0 ,0), localTime)
    }

    override fun toJson(value: Any): String {
        val duration: Duration = value as Duration
        return """"${duration.toDisplay()}""""
    }
}
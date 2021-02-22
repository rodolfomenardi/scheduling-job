package br.com.menardi.scheduling.converters

import com.beust.klaxon.JsonValue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.lang.reflect.Type
import java.time.Duration
import java.time.LocalDateTime
import kotlin.test.assertEquals

internal class DurationConverterTest {
    private val converter = DurationConverter()

    @Test
    fun deveRetornarVerdadeiroSeClasseForDuration() {
        assertTrue(converter.canConvert(Duration::class.java))
    }

    @Test
    fun deveRetornarFalsoSeClasseNaoForDuration() {
        assertFalse(converter.canConvert(LocalDateTime::class.java))
    }

    @Test
    fun deveGerarStringAoReceberUmDuration() {
        val twoHoursAndTenMinutesAndTwoSeconds: Long = 7802
        val stringDuration = converter.toJson(Duration.ofSeconds(twoHoursAndTenMinutesAndTwoSeconds))

        assertEquals(""""02:10:02"""", stringDuration)
    }
}
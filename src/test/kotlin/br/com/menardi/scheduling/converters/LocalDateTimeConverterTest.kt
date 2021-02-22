package br.com.menardi.scheduling.converters

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.time.Duration
import java.time.LocalDateTime

internal class LocalDateTimeConverterTest {
    private val converter = LocalDateTimeConverter()

    @Test
    fun deveRetornarVerdadeiroSeClasseForLocalDateTime() {
        assertTrue(converter.canConvert(LocalDateTime::class.java))
    }

    @Test
    fun deveRetornarFalsoSeClasseNaoForLocalDateTime() {
        assertFalse(converter.canConvert(Duration::class.java))
    }

    @Test
    fun deveGerarStringAoReceberUmLocalDateTime() {
        val stringDuration = converter.toJson(LocalDateTime.of(2021, 2, 22, 10, 6, 13))

        kotlin.test.assertEquals(""""2021-02-22 10:06:13"""", stringDuration)
    }
}
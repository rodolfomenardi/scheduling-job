package br.com.menardi.scheduling.extensions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration

internal class DurationExtensionsTest {

    @Test
    fun deveRetornarDuracaoApenasComHorasFormatada() {
        val hourDuration = Duration.ofHours(5)
        val formattedDuration = hourDuration.toDisplay()

        assertEquals("05:00:00", formattedDuration)
    }

    @Test
    fun deveRetornarDuracaoApenasComMinutosFormatada() {
        val hourDuration = Duration.ofMinutes(50)
        val formattedDuration = hourDuration.toDisplay()

        assertEquals("00:50:00", formattedDuration)
    }

    @Test
    fun deveRetornarDuracaoApenasComSegundosFormatada() {
        val hourDuration = Duration.ofSeconds(52)
        val formattedDuration = hourDuration.toDisplay()

        assertEquals("00:00:52", formattedDuration)
    }

    @Test
    fun deveRetornarDuracaoCompletaFormatada() {
        val hourDuration = Duration.ofSeconds(14475)
        val formattedDuration = hourDuration.toDisplay()

        assertEquals("04:01:15", formattedDuration)
    }

    @Test
    fun deveRetornarDuracaoZeradaFormatada() {
        val hourDuration = Duration.ZERO
        val formattedDuration = hourDuration.toDisplay()

        assertEquals("00:00:00", formattedDuration)
    }
}
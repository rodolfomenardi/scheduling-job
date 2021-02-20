package br.com.menardi.scheduling.extensions

import br.com.menardi.scheduling.model.Job
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class IterableSumByDurationExtensionTest {

    @Test
    fun deveRetornarUmaDuracaoPositiva() {
        val hours: Long = 6
        val minutes: Long = 6
        val seconds: Long = 10

        val jobs = listOf(
            Job(1, "Importação de arquivos de fundos", LocalDateTime.of(2019, 11, 10, 12, 0, 0), Duration.ofHours(hours)),
            Job(2, "Importação de dados da Base Legada", LocalDateTime.of(2019, 11, 11, 12, 0, 0), Duration.ofMinutes(minutes)),
            Job(3, "Importação de dados de integração", LocalDateTime.of(2019, 11, 11, 8, 0, 0), Duration.ofSeconds(seconds))
        )


        val totalDuration = jobs.sumByDuration { it.estimatedDuration }
        val totalSeconds = (hours * 60 * 60) + (minutes * 60) + seconds
        assertEquals(totalSeconds, totalDuration.seconds)
    }

    @Test
    fun deveRetornarZeroSegundos() {
        val jobs = listOf(
            Job(1, "Importação de arquivos de fundos", LocalDateTime.of(2019, 11, 10, 12, 0, 0), Duration.ZERO),
            Job(2, "Importação de dados da Base Legada", LocalDateTime.of(2019, 11, 11, 12, 0, 0), Duration.ZERO),
            Job(3, "Importação de dados de integração", LocalDateTime.of(2019, 11, 11, 8, 0, 0), Duration.ZERO)
        )

        val totalDuration = jobs.sumByDuration { it.estimatedDuration }
        assertEquals(0, totalDuration.seconds)
    }

    @Test
    fun deveRetornarZeroSegundosComListaVazia() {
        val jobs = listOf<Job>()

        val totalDuration = jobs.sumByDuration { it.estimatedDuration }
        assertEquals(0, totalDuration.seconds)
    }
}
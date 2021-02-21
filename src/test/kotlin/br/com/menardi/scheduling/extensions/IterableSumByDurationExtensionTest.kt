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
            Job(
                id = 1,
                description = "Importação de arquivos de fundos",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 10, 12, 0, 0),
                estimatedDuration = Duration.ofHours(hours)
            ),
            Job(
                id = 2,
                description = "Importação de dados da Base Legada",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 11, 12, 0, 0),
                estimatedDuration = Duration.ofMinutes(minutes)
            ),
            Job(
                id = 3,
                description = "Importação de dados de integração",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 11, 8, 0, 0),
                estimatedDuration = Duration.ofSeconds(seconds)
            )
        )


        val totalDuration = jobs.sumByDuration { it.estimatedDuration }
        val totalSeconds = (hours * 60 * 60) + (minutes * 60) + seconds
        assertEquals(totalSeconds, totalDuration.seconds)
    }

    @Test
    fun deveRetornarZeroSegundos() {
        val jobs = listOf(
            Job(
                id = 1,
                description = "Importação de arquivos de fundos",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 10, 12, 0, 0),
                estimatedDuration = Duration.ZERO
            ),
            Job(
                id = 2,
                description = "Importação de dados da Base Legada",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 11, 12, 0, 0),
                estimatedDuration = Duration.ZERO
            ),
            Job(
                id = 3,
                description = "Importação de dados de integração",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 11, 8, 0, 0),
                estimatedDuration = Duration.ZERO
            )
        )

        val totalDuration = jobs.sumByDuration { it.estimatedDuration }
        assertEquals(0, totalDuration.seconds)
    }

    @Test
    fun deveRetornarZeroSegundosComListaVazia() {
        val jobs = emptyList<Job>()

        val totalDuration = jobs.sumByDuration { it.estimatedDuration }
        assertEquals(0, totalDuration.seconds)
    }
}
package br.com.menardi.scheduling.jobValidators

import br.com.menardi.scheduling.exceptions.MaxDurationException
import br.com.menardi.scheduling.model.Job
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Duration
import java.time.LocalDateTime

internal class MaxDurationJobValidatorTest {
    private val maxDuration = Duration.ofHours(8)
    private val validator = MaxDurationJobValidator(maxDuration)


    @Test
    fun deveLancarExceptionComJobComMaisDeOitoHorasDeDuracao() {
        val eightHoursAndOneSecond: Long = 28801

        val job = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
            estimatedDuration = Duration.ofSeconds(eightHoursAndOneSecond)
        )

        assertThrows(MaxDurationException::class.java) { validator.validate(job = job) }
    }

    @Test
    fun naoDeveLancarExceptionComJobComDuracaoDeOitoHoras() {
        val job = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
            estimatedDuration = Duration.ofHours(8)
        )

        assertDoesNotThrow { validator.validate(job = job) }
    }

    @Test
    fun naoDeveLancarExceptionComJobComMenosDeOitoHorasDeDuracao() {
        val sevenHoursAndFiftyNineMinutesAndFiftyNineSeconds: Long = 28799
        val job = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
            estimatedDuration = Duration.ofSeconds(sevenHoursAndFiftyNineMinutesAndFiftyNineSeconds)
        )

        assertDoesNotThrow { validator.validate(job = job) }
    }
}
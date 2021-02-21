package br.com.menardi.scheduling.jobValidators

import br.com.menardi.scheduling.exceptions.InvalidDurationJobException
import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Duration
import java.time.LocalDateTime

internal class JobDurationJobValidatorTest {
    private val executionWindow = ExecutionWindow(
        start = LocalDateTime.of(2021, 10, 10, 8, 0, 0),
        end = LocalDateTime.of(2021, 10, 11, 12, 0, 0)
    )

    private val validator = JobDurationJobValidator(executionWindow)

    @Test
    fun deveLancarExceptionComJobComDataMaximaEEstimativaIncompativeis() {
        val twoHoursAndOneSecond: Long = 7201

        val job = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofSeconds(twoHoursAndOneSecond)
        )

        assertThrows(InvalidDurationJobException::class.java) { validator.validate(job = job) }
    }

    @Test
    fun naoDeveLancarExceptionComJobComDataMaximaEEstimativaExatas() {
        val twoHours: Long = 2

        val job = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofHours(twoHours)
        )

        assertDoesNotThrow { validator.validate(job = job) }
    }

    @Test
    fun naoDeveLancarExceptionComJobComDataMaximaEEstimativaMenores() {
        val oneHourAndFiftyNineMinutesAndFiftyNineSeconds: Long = 7199

        val job = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofSeconds(oneHourAndFiftyNineMinutesAndFiftyNineSeconds)
        )

        assertDoesNotThrow { validator.validate(job = job) }
    }
}
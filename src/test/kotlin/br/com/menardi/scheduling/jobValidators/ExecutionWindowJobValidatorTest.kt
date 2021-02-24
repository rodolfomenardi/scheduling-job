package br.com.menardi.scheduling.jobValidators

import br.com.menardi.scheduling.exceptions.JobOutOfExecutionWindowException
import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.time.Duration
import java.time.LocalDateTime

internal class ExecutionWindowJobValidatorTest {
    private val executionWindow = ExecutionWindow(
        start = LocalDateTime.of(2021, 10, 11, 8, 0, 0),
        end = LocalDateTime.of(2021, 10, 11, 12, 0, 0)
    )

    private val validator = ExecutionWindowJobValidator(executionWindow)

    @Test
    fun deveLancarExceptionComJobAntesDaJanela() {
        val oneHour: Long = 1
        val jobBeforeExecutionWindow = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 7, 59, 59),
            estimatedDuration = Duration.ofHours(oneHour)
        )

        assertThrows(JobOutOfExecutionWindowException::class.java) { validator.validate(job = jobBeforeExecutionWindow) }
    }

    @Test
    fun deveLancarExceptionComJobComDuracaoMaiorQueAJanela() {
        val fourHoursAndOneSecond: Long = 14401
        val jobAfterExecutionWindow = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 14, 0, 0),
            estimatedDuration = Duration.ofSeconds(fourHoursAndOneSecond)
        )

        assertThrows(JobOutOfExecutionWindowException::class.java) { validator.validate(job = jobAfterExecutionWindow) }
    }

    @Test
    fun naoDeveLancarExceptionComJobExatamenteDentroDaJanela() {
        val fourHours: Long = 4

        val jobInExecutionWindow = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 8, 0, 0),
            estimatedDuration = Duration.ofHours(fourHours)
        )

        assertDoesNotThrow { validator.validate(job = jobInExecutionWindow) }
    }

    @Test
    fun naoDeveLancarExceptionComJobMenorQueAJanela() {
        val threeHoursAndFiftyNineMinutesAndFiftyNineSeconds: Long = 14399

        val jobInExecutionWindow = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 8, 0, 0),
            estimatedDuration = Duration.ofSeconds(threeHoursAndFiftyNineMinutesAndFiftyNineSeconds)
        )

        assertDoesNotThrow { validator.validate(job = jobInExecutionWindow) }
    }
}
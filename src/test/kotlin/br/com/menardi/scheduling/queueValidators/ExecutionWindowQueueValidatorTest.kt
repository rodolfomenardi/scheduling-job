package br.com.menardi.scheduling.queueValidators

import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class ExecutionWindowQueueValidatorTest {
    private val executionWindow = ExecutionWindow(
        start = LocalDateTime.of(2021, 10, 10, 8, 0, 0),
        end = LocalDateTime.of(2021, 10, 10, 12, 0, 0)
    )

    private val validator = ExecutionWindowQueueValidator(executionWindow)

    @Test
    fun deveSerValidoDuracaoIgualAJanelaDeExecucaoComDuracaoCorrenteZerada() {
        val currentZeroDuration = Duration.ZERO
        val jobWithFourHoursDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
            estimatedDuration = Duration.ofHours(4)
        )

        val isValidCurrentDurationZero = validator.validate(currentZeroDuration, jobWithFourHoursDuration)
        assertTrue(isValidCurrentDurationZero)
    }

    @Test
    fun deveSerValidoDuracaoIgualAJanelaDeExecucaoComDuracaoCorrenteDeUmaHora() {
        val currentOneHourDuration = Duration.ofHours(1)
        val jobWithThreeHoursDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
            estimatedDuration = Duration.ofHours(3)
        )

        val isValidCurrentDurationOneHour = validator.validate(currentOneHourDuration, jobWithThreeHoursDuration)
        assertTrue(isValidCurrentDurationOneHour)
    }

    @Test
    fun deveSerValidoDuracaoMenorQueAJanelaDeExecucaoComDuracaoCorrenteZerada() {
        val currentZeroDuration = Duration.ZERO
        val threeHoursAndFiftyNineMinutesAndFiftyNineSeconds: Long = 14339
        val jobWithThreeHoursAndFiftyNineMinutesAndFiftyNineSecondsDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
            estimatedDuration = Duration.ofSeconds(threeHoursAndFiftyNineMinutesAndFiftyNineSeconds)
        )

        val isValidCurrentDurationZero =
            validator.validate(currentZeroDuration, jobWithThreeHoursAndFiftyNineMinutesAndFiftyNineSecondsDuration)
        assertTrue(isValidCurrentDurationZero)
    }

    @Test
    fun deveSerValidoDuracaoMenorQueAJanelaDeExecucaoComDuracaoCorrenteDeUmaHora() {
        val currentOneHourDuration = Duration.ofHours(1)
        val twoHoursAndFiftyNineMinutesAndFiftyNineSeconds: Long = 10739
        val jobWithTwoHoursAndFiftyNineMinutesAndFiftyNineSecondsDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
            estimatedDuration = Duration.ofSeconds(twoHoursAndFiftyNineMinutesAndFiftyNineSeconds)
        )

        val isValidCurrentDurationOneHour =
            validator.validate(currentOneHourDuration, jobWithTwoHoursAndFiftyNineMinutesAndFiftyNineSecondsDuration)
        assertTrue(isValidCurrentDurationOneHour)
    }

    @Test
    fun naoDeveSerValidoDuracaoMaiorQueAJanelaDeExecucaoComDuracaoCorrenteZerada() {
        val currentZeroDuration = Duration.ZERO
        val fourHoursAndOneSecond: Long = 14401
        val jobWithFourHoursAndOneSecondDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
            estimatedDuration = Duration.ofSeconds(fourHoursAndOneSecond)
        )

        val isValidCurrentDurationZero = validator.validate(currentZeroDuration, jobWithFourHoursAndOneSecondDuration)
        assertFalse(isValidCurrentDurationZero)
    }

    @Test
    fun naoDeveSerValidoDuracaoMaiorQueAJanelaDeExecucaoComDuracaoCorrenteDeUmaHora() {
        val currentOneHourDuration = Duration.ofHours(1)
        val threeHoursAndOneSecond: Long = 14401
        val jobWithThreeHoursAndOneSecondDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
            estimatedDuration = Duration.ofSeconds(threeHoursAndOneSecond)
        )

        val isValid = validator.validate(currentOneHourDuration, jobWithThreeHoursAndOneSecondDuration)
        assertFalse(isValid)
    }
}
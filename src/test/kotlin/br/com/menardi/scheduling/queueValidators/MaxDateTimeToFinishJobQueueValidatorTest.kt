package br.com.menardi.scheduling.queueValidators

import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class MaxDateTimeToFinishJobQueueValidatorTest {
    private val executionWindow = ExecutionWindow(
        start = LocalDateTime.of(2021, 10, 10, 8, 0, 0),
        end = LocalDateTime.of(2021, 10, 10, 12, 0, 0)
    )

    private val validator = MaxDateTimeToFinishJobQueueValidator(executionWindow)

    @Test
    fun deveValidarSeADataEstimadaDeConclusaoForIgualADataMaximaDaJobComDuracaoCorrenteZerada() {
        val currentZeroDuration = Duration.ZERO
        val jobWithTwoHoursDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofHours(2)
        )

        val isValidCurrentDurationZero = validator.validate(currentZeroDuration, jobWithTwoHoursDuration)
        assertTrue(isValidCurrentDurationZero)
    }

    @Test
    fun deveValidarSeADataEstimadaDeConclusaoForIgualADataMaximaDaJobComDuracaoCorrenteDeUmaHora() {
        val currentOneHourDuration = Duration.ofHours(1)
        val jobWithOneHourDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofHours(1)
        )

        val isValidCurrentDurationOneHour = validator.validate(currentOneHourDuration, jobWithOneHourDuration)
        assertTrue(isValidCurrentDurationOneHour)
    }

    @Test
    fun deveValidarSeADataEstimadaDeConclusaoForMenorQueADataMaximaDaJobComDuracaoCorrenteZerada() {
        val currentZeroDuration = Duration.ZERO
        val oneHourAndFiftyNineMinutesAndFiftyNineSeconds: Long = 7199
        val jobWithOneHourAndFiftyNineMinutesAndFiftyNineSecondsDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofSeconds(oneHourAndFiftyNineMinutesAndFiftyNineSeconds)
        )

        val isValidCurrentDurationZero =
            validator.validate(currentZeroDuration, jobWithOneHourAndFiftyNineMinutesAndFiftyNineSecondsDuration)
        assertTrue(isValidCurrentDurationZero)
    }

    @Test
    fun deveValidarSeADataEstimadaDeConclusaoForMenorQueADataMaximaDaJobComDuracaoCorrenteDeUmaHora() {
        val currentOneHourDuration = Duration.ofHours(1)
        val fiftyNineMinutesAndFiftyNineSeconds: Long = 3599
        val jobWithFiftyNineMinutesAndFiftyNineSecondsDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofSeconds(fiftyNineMinutesAndFiftyNineSeconds)
        )

        val isValidCurrentDurationOneHour =
            validator.validate(currentOneHourDuration, jobWithFiftyNineMinutesAndFiftyNineSecondsDuration)
        assertTrue(isValidCurrentDurationOneHour)
    }

    @Test
    fun naoDeveValidarSeADataEstimadaDeConclusaoForMaiorQueADataMaximaDaJobComDuracaoCorrenteZerada() {
        val currentZeroDuration = Duration.ZERO
        val twoHoursAndOneSecond: Long = 7201
        val jobWithTwoHoursAndOneSecondDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofSeconds(twoHoursAndOneSecond)
        )

        val isValidCurrentDurationZero = validator.validate(currentZeroDuration, jobWithTwoHoursAndOneSecondDuration)
        assertFalse(isValidCurrentDurationZero)
    }

    @Test
    fun naoDeveValidarSeADataEstimadaDeConclusaoForMaiorQueADataMaximaDaJobComDuracaoCorrenteDeUmaHora() {
        val currentOneHourDuration = Duration.ofHours(1)
        val oneHourAndOneSecond: Long = 3601
        val jobWithOneHourAndOneSecondDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofSeconds(oneHourAndOneSecond)
        )

        val isValidCurrentDurationOneHour =
            validator.validate(currentOneHourDuration, jobWithOneHourAndOneSecondDuration)
        assertFalse(isValidCurrentDurationOneHour)
    }
}
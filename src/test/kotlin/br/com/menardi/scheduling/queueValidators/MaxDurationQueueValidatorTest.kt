package br.com.menardi.scheduling.queueValidators

import br.com.menardi.scheduling.model.Job
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

internal class MaxDurationQueueValidatorTest {
    private val maxDuration = Duration.ofHours(8)
    private val validator = MaxDurationQueueValidator(maxDuration)

    @Test
    fun deveValidarSeADuracaoDaJobForIgualADuracaoMaximaPermitidaComDuracaoCorreteZerada() {
        val currentZeroDuration = Duration.ZERO
        val jobWithEightHoursDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofHours(8)
        )

        val isValidCurrentDurationZero = validator.validate(currentZeroDuration, jobWithEightHoursDuration)
        assertTrue(isValidCurrentDurationZero)
    }

    @Test
    fun deveValidarSeADuracaoDaJobForIgualADuracaoMaximaPermitidaComDuracaoCorreteDeUmaHora() {
        val currentOneHourDuration = Duration.ofHours(1)
        val jobWithSevenHourDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofHours(7)
        )

        val isValidCurrentDurationOneHour = validator.validate(currentOneHourDuration, jobWithSevenHourDuration)
        assertTrue(isValidCurrentDurationOneHour)
    }

    @Test
    fun deveValidarSeADuracaoDaJobForMenorQueADuracaoMaximaPermitidaComDuracaoCorreteZerada() {
        val currentZeroDuration = Duration.ZERO
        val sevenHours59Minutes59Seconds: Long = 28799
        val jobWithSevenHours59Minutes59SecondsDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofSeconds(sevenHours59Minutes59Seconds)
        )

        val isValidCurrentDurationZero =
            validator.validate(currentZeroDuration, jobWithSevenHours59Minutes59SecondsDuration)
        assertTrue(isValidCurrentDurationZero)
    }

    @Test
    fun deveValidarSeADuracaoDaJobForMenorQueADuracaoMaximaPermitidaComDuracaoCorreteDeUmaHora() {
        val currentOneHourDuration = Duration.ofHours(1)
        val sixHours59Minutes59Seconds: Long = 25199
        val jobWithSixHours59Minutes59SecondsDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofSeconds(sixHours59Minutes59Seconds)
        )

        val isValidCurrentDurationOneHour =
            validator.validate(currentOneHourDuration, jobWithSixHours59Minutes59SecondsDuration)
        assertTrue(isValidCurrentDurationOneHour)
    }

    @Test
    fun naoDeveValidarSeADuracaoDaJobForMaiorQueADuracaoMaximaPermitidaComDuracaoCorreteZerada() {
        val currentZeroDuration = Duration.ZERO
        val eightHoursAndOneSecond: Long = 28801
        val jobWithEightHoursAndOneSecondDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofSeconds(eightHoursAndOneSecond)
        )

        val isValidCurrentDurationZero = validator.validate(currentZeroDuration, jobWithEightHoursAndOneSecondDuration)
        assertFalse(isValidCurrentDurationZero)
    }

    @Test
    fun naoDeveValidarSeADuracaoDaJobForMaiorQueADuracaoMaximaPermitidaComDuracaoCorreteComUmaHora() {
        val currentOneHourDuration = Duration.ofHours(1)
        val sevenHoursAndOneSecond: Long = 25201
        val jobWithSevenHoursAndOneSecondDuration = Job(
            id = 1,
            description = "Emitir notas",
            maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
            estimatedDuration = Duration.ofSeconds(sevenHoursAndOneSecond)
        )

        val isValidCurrentDurationOneHour =
            validator.validate(currentOneHourDuration, jobWithSevenHoursAndOneSecondDuration)
        assertFalse(isValidCurrentDurationOneHour)
    }
}
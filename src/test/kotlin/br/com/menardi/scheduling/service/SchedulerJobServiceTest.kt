package br.com.menardi.scheduling.service

import br.com.menardi.scheduling.exceptions.InvalidDurationJobException
import br.com.menardi.scheduling.exceptions.JobOutOfExecutionWindowException
import br.com.menardi.scheduling.exceptions.MaxDurationException
import br.com.menardi.scheduling.extensions.sumByDuration
import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

class SchedulerJobServiceTest {
    @Test
    fun deveLancarExceptionComJobForaDaJanela() {
        val executionWindow = ExecutionWindow(
            start = LocalDateTime.of(2021, 10, 11, 8, 0, 0),
            end = LocalDateTime.of(2021, 10, 11, 12, 0, 0)
        )

        val service = SchedulerJobService(executionWindow)

        val oneHour: Long = 1
        val jobsBeforeExecutionWindow = listOf(
            Job(
                id = 1,
                description = "Emitir notas",
                maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 7, 59, 59),
                estimatedDuration = Duration.ofHours(oneHour)
            )
        )

        assertThrows(JobOutOfExecutionWindowException::class.java) { service.getListsToExecution(jobs = jobsBeforeExecutionWindow) }

        val fourHoursAndOneSecond: Long = 14401
        val jobsAfterExecutionWindow = listOf(
            Job(
                id = 1,
                description = "Emitir notas",
                maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
                estimatedDuration = Duration.ofSeconds(fourHoursAndOneSecond)
            )
        )

        assertThrows(JobOutOfExecutionWindowException::class.java) { service.getListsToExecution(jobs = jobsAfterExecutionWindow) }
    }

    @Test
    fun deveLancarExceptionComJobComDataMaximaEEstimativaIncompativeis() {
        val executionWindow = ExecutionWindow(
            start = LocalDateTime.of(2021, 10, 10, 8, 0, 0),
            end = LocalDateTime.of(2021, 10, 11, 12, 0, 0)
        )

        val service = SchedulerJobService(executionWindow)

        val twoHoursAndOneSecond: Long = 7201
        val jobs = listOf(
            Job(
                id = 1,
                description = "Emitir notas",
                maxDateTimeToFinish = LocalDateTime.of(2021, 10, 10, 10, 0, 0),
                estimatedDuration = Duration.ofSeconds(twoHoursAndOneSecond)
            )
        )

        assertThrows(InvalidDurationJobException::class.java) { service.getListsToExecution(jobs = jobs) }
    }

    @Test
    fun deveLancarExceptionComJobComMaisDeOitoHorasDeDuracao() {
        val executionWindow = ExecutionWindow(
            start = LocalDateTime.of(2021, 10, 10, 8, 0, 0),
            end = LocalDateTime.of(2021, 10, 11, 12, 0, 0)
        )

        val service = SchedulerJobService(executionWindow)

        val eightHoursAndOneSecond: Long = 28801
        val jobs = listOf(
            Job(
                id = 1,
                description = "Emitir notas",
                maxDateTimeToFinish = LocalDateTime.of(2021, 10, 11, 12, 0, 0),
                estimatedDuration = Duration.ofSeconds(eightHoursAndOneSecond)
            )
        )

        assertThrows(MaxDurationException::class.java) { service.getListsToExecution(jobs = jobs) }
    }

    @Test
    fun deveRetornarListasComJobsComDuracaoMaximaDeOitoHoras() {
        val jobs = listOf(
            Job(
                id = 1,
                description = "Importação de arquivos de fundos",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 10, 12, 0, 0),
                estimatedDuration = Duration.ofHours(2)
            ),
            Job(
                id = 2,
                description = "Importação de dados da Base Legada",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 11, 12, 0, 0),
                estimatedDuration = Duration.ofHours(6)
            ),
            Job(
                id = 3,
                description = "Importação de dados de integração",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 11, 8, 0, 0),
                estimatedDuration = Duration.ofHours(6)
            )
        )

        val executionWindow = ExecutionWindow(
            start = LocalDateTime.of(2019, 11, 10, 9, 0, 0),
            end = LocalDateTime.of(2019, 11, 11, 12, 0, 0)
        )

        val service = SchedulerJobService(executionWindow)

        val listsToExecution = service.getListsToExecution(jobs)
        assertEquals(2, listsToExecution.size)

        listsToExecution.forEach { jobsList ->
            val totalDuration = jobsList.sumByDuration { it.estimatedDuration }
            val eightHoursInSeconds = 28800
            assertTrue(totalDuration.seconds <= eightHoursInSeconds)
        }
    }

    @Test
    fun deveSerRespeitadaADataDeConclusaoDaJob() {
        val jobs = listOf(
            Job(
                id = 1,
                description = "Importação de arquivos de fundos",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 10, 11, 0, 0),
                estimatedDuration = Duration.ofHours(2)
            ),
            Job(
                id = 2,
                description = "Importação de dados da Base Legada",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 11, 12, 0, 0),
                estimatedDuration = Duration.ofHours(6)
            ),
            Job(
                id = 3,
                description = "Importação de dados de integração",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 10, 15, 0, 0),
                estimatedDuration = Duration.ofHours(6)
            )
        )

        val executionWindow = ExecutionWindow(
            start = LocalDateTime.of(2019, 11, 10, 9, 0, 0),
            end = LocalDateTime.of(2019, 11, 11, 12, 0, 0)
        )

        val service = SchedulerJobService(executionWindow)

        val listsToExecution = service.getListsToExecution(jobs)
        assertEquals(2, listsToExecution.size)

        listsToExecution.forEach { jobsList ->
            var totalDuration = Duration.ZERO
            jobsList.forEach { job ->
                totalDuration = totalDuration.plus(job.estimatedDuration)
                val endExecutionEstimated = executionWindow.start.plus(totalDuration)
                assertFalse(endExecutionEstimated.isAfter(job.maxDateTimeToFinish))
            }
        }
    }

    @Test
    fun deveSerRespeitadaAJanelaDeExecucao() {
        val jobs = listOf(
            Job(
                id = 1,
                description = "Importação de arquivos de fundos",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 10, 15, 0, 0),
                estimatedDuration = Duration.ofHours(2)
            ),
            Job(
                id = 2,
                description = "Importação de dados da Base Legada",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 10, 17, 0, 0),
                estimatedDuration = Duration.ofHours(6)
            ),
            Job(
                id = 3,
                description = "Importação de dados de integração",
                maxDateTimeToFinish = LocalDateTime.of(2019, 11, 10, 18, 0, 0),
                estimatedDuration = Duration.ofHours(4)
            )
        )

        val executionWindow = ExecutionWindow(
            start = LocalDateTime.of(2019, 11, 10, 9, 0, 0),
            end = LocalDateTime.of(2019, 11, 10, 15, 0, 0)
        )

        val service = SchedulerJobService(executionWindow)

        val listsToExecution = service.getListsToExecution(jobs)
        assertEquals(2, listsToExecution.size)

        listsToExecution.forEach { jobsList ->
            var totalDuration = Duration.ZERO
            jobsList.forEach { job ->
                totalDuration = totalDuration.plus(job.estimatedDuration)
                val estimatedEndExecution = executionWindow.start.plus(totalDuration)
                assertFalse(estimatedEndExecution.isAfter(executionWindow.end))
            }
        }
    }
}
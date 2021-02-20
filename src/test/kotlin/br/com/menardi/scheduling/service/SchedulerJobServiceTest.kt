package br.com.menardi.scheduling.service

import br.com.menardi.scheduling.exceptions.InvalidDurationJobException
import br.com.menardi.scheduling.exceptions.JobOutOfExecutionWindowException
import br.com.menardi.scheduling.exceptions.MaxDurationException
import br.com.menardi.scheduling.extensions.sumByDuration
import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

class SchedulerJobServiceTest {
    @Test
    fun deveLancarExceptionComJobComMaisDeOitoHoras() {
        val startDateTime = LocalDateTime.of(2021, 10, 10, 8, 0, 0)
        val endDateTime = LocalDateTime.of(2021, 10, 11, 12, 0, 0)
        val executionWindow = ExecutionWindow(startDateTime, endDateTime)

        val service = SchedulerJobService(executionWindow)

        val jobs = listOf(Job(1, "Emitir notas", LocalDateTime.of(2021, 10, 11, 12, 0, 0), Duration.ofMinutes(481)))
        assertThrows(MaxDurationException::class.java) { service.validate(jobs) }
    }

    @Test
    fun deveLancarExceptionComJobForaDaJanela() {
        val startDateTime = LocalDateTime.of(2021, 10, 11, 8, 0, 0)
        val endDateTime = LocalDateTime.of(2021, 10, 11, 12, 0, 0)
        val executionWindow = ExecutionWindow(startDateTime, endDateTime)

        val service = SchedulerJobService(executionWindow)

        val jobsBeforeExecutionWindow =
            listOf(Job(1, "Emitir notas", LocalDateTime.of(2021, 10, 11, 7, 59, 59), Duration.ofHours(1)))

        assertThrows(JobOutOfExecutionWindowException::class.java) { service.validate(jobsBeforeExecutionWindow) }

        val jobsAfterExecutionWindow =
            listOf(Job(1, "Emitir notas", LocalDateTime.of(2021, 10, 11, 12, 0, 1), Duration.ofHours(1)))
        assertThrows(JobOutOfExecutionWindowException::class.java) { service.validate(jobsAfterExecutionWindow) }
    }

    @Test
    fun deveLancarExceptionComJobComDataMaximaEEstimativaIncompativeis() {
        val startDateTime = LocalDateTime.of(2021, 10, 10, 8, 0, 0)
        val endDateTime = LocalDateTime.of(2021, 10, 11, 12, 0, 0)
        val executionWindow = ExecutionWindow(startDateTime, endDateTime)

        val service = SchedulerJobService(executionWindow)

        val jobs = listOf(Job(1, "Emitir notas", LocalDateTime.of(2021, 10, 10, 10, 0, 0), Duration.ofMinutes(121)))
        assertThrows(InvalidDurationJobException::class.java) { service.validate(jobs) }
    }

    @Test
    fun deveRetornarListasComJobsComDuracaoMaximaDeOitoHoras() {
        val jobs = listOf(
            Job(1, "Importação de arquivos de fundos", LocalDateTime.of(2019, 11, 10, 12, 0, 0), Duration.ofHours(2)),
            Job(2, "Importação de dados da Base Legada", LocalDateTime.of(2019, 11, 11, 12, 0, 0), Duration.ofHours(6)),
            Job(3, "Importação de dados de integração", LocalDateTime.of(2019, 11, 11, 8, 0, 0), Duration.ofHours(6))
        )

        val startDateTime = LocalDateTime.of(2019, 11, 10, 9, 0, 0)
        val endDateTime = LocalDateTime.of(2019, 11, 11, 12, 0, 0)
        val executionWindow = ExecutionWindow(startDateTime, endDateTime)

        val service = SchedulerJobService(executionWindow)

        service.validate(jobs)

        val listsToExecution = service.getListsToExecution(jobs)
        assertEquals(2, listsToExecution.size)

        val listJob1 = listsToExecution[0]
        assertEquals(2, listJob1.size)
        val totalDuration1 = listJob1.sumByDuration { it.estimatedDuration }
        assertEquals(8*60*60, totalDuration1.seconds)

        val listJob2 = listsToExecution[1]
        assertEquals(listJob2.size, 1)
        val totalDuration2 = listJob2.sumByDuration { it.estimatedDuration }
        assertEquals(6*60*60, totalDuration2.seconds)
    }
}
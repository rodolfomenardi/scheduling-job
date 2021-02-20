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
            listOf(Job(1, "Emitir notas", LocalDateTime.of(2021, 10, 11, 12, 0, 0), Duration.ofSeconds(14401)))
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

        val jobsList1 = listsToExecution[0]
        assertEquals(2, jobsList1.size)
        val totalDuration1 = jobsList1.sumByDuration { it.estimatedDuration }
        assertEquals(8*60*60, totalDuration1.seconds)

        val jobsList2 = listsToExecution[1]
        assertEquals(jobsList2.size, 1)
        val totalDuration2 = jobsList2.sumByDuration { it.estimatedDuration }
        assertEquals(6*60*60, totalDuration2.seconds)
    }

    @Test
    fun deveSerRespeitadaADataDeConclusaoDaJob() {
        val jobs = listOf(
            Job(1, "Importação de arquivos de fundos", LocalDateTime.of(2019, 11, 10, 11, 0, 0), Duration.ofHours(2)),
            Job(2, "Importação de dados da Base Legada", LocalDateTime.of(2019, 11, 11, 12, 0, 0), Duration.ofHours(6)),
            Job(3, "Importação de dados de integração", LocalDateTime.of(2019, 11, 10, 15, 0, 0), Duration.ofHours(6))
        )

        val startDateTime = LocalDateTime.of(2019, 11, 10, 9, 0, 0)
        val endDateTime = LocalDateTime.of(2019, 11, 11, 12, 0, 0)
        val executionWindow = ExecutionWindow(startDateTime, endDateTime)

        val service = SchedulerJobService(executionWindow)

        service.validate(jobs)

        val listsToExecution = service.getListsToExecution(jobs)
        assertEquals(2, listsToExecution.size)

        val jobList1 = listsToExecution[0]
        assertEquals(2, jobList1.size)

        val job1A = jobList1[0]
        val endExecutionJob1A = executionWindow.start.plus(job1A.estimatedDuration)
        assertTrue(endExecutionJob1A.isBefore(job1A.maxDateTimeToFinish) || endExecutionJob1A.isEqual(job1A.maxDateTimeToFinish))

        val job1B = jobList1[1]
        val endExecutionJob1B = executionWindow.start.plus(job1A.estimatedDuration).plus(job1B.estimatedDuration)
        assertTrue(endExecutionJob1B.isBefore(job1B.maxDateTimeToFinish) || endExecutionJob1B.isEqual(job1B.maxDateTimeToFinish))

        val jobList2 = listsToExecution[1]
        assertEquals(1, jobList2.size)

        val job2A = jobList2[0]
        val endExecutionJob2A = executionWindow.start.plus(job2A.estimatedDuration)
        assertTrue(endExecutionJob2A.isBefore(job2A.maxDateTimeToFinish) || endExecutionJob2A.isEqual(job2A.maxDateTimeToFinish))
    }

    @Test
    fun deveSerRespeitadaAJanelaDeExecucao() {
        val jobs = listOf(
            Job(1, "Importação de arquivos de fundos", LocalDateTime.of(2019, 11, 10, 15, 0, 0), Duration.ofHours(2)),
            Job(2, "Importação de dados da Base Legada", LocalDateTime.of(2019, 11, 10, 17, 0, 0), Duration.ofHours(6)),
            Job(3, "Importação de dados de integração", LocalDateTime.of(2019, 11, 10, 18, 0, 0), Duration.ofHours(4))
        )

        val startDateTime = LocalDateTime.of(2019, 11, 10, 9, 0, 0)
        val endDateTime = LocalDateTime.of(2019, 11, 10, 15, 0, 0)
        val executionWindow = ExecutionWindow(startDateTime, endDateTime)

        val service = SchedulerJobService(executionWindow)

        service.validate(jobs)

        val listsToExecution = service.getListsToExecution(jobs)
        assertEquals(2, listsToExecution.size)

        val jobsList1 = listsToExecution[0]
        assertEquals(2, jobsList1.size)
        val job1A = jobsList1[0]
        val estimatedEndExecution1A = executionWindow.start.plus(job1A.estimatedDuration)
        assertFalse(estimatedEndExecution1A.isAfter(executionWindow.end))

        val job1B = jobsList1[1]
        val estimatedEndExecution1B = executionWindow.start.plus(job1A.estimatedDuration).plus(job1B.estimatedDuration)
        assertFalse(estimatedEndExecution1B.isAfter(executionWindow.end))


        val jobsList2 = listsToExecution[1]
        assertEquals(1, jobsList2.size)
        val job2A = jobsList2[0]
        val estimatedEndExecution2A = executionWindow.start.plus(job2A.estimatedDuration)
        assertFalse(estimatedEndExecution2A.isAfter(executionWindow.end))
    }
}
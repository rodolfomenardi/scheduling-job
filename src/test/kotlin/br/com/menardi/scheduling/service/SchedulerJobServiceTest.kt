package br.com.menardi.scheduling.service

import br.com.menardi.scheduling.exceptions.InvalidDurationJobException
import br.com.menardi.scheduling.exceptions.JobOutOfExecutionWindowException
import br.com.menardi.scheduling.exceptions.MaxDurationException
import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

class SchedulerJobServiceTest {
    @Test
    fun deveLancarExceptionComJobComMaisDeOitoHoras() {
        val service = SchedulerJobService();

        val startDateTime = LocalDateTime.of(2021, 10, 10, 8, 0, 0);
        val endDateTime = LocalDateTime.of(2021, 10, 11, 12, 0, 0);
        val executionWindow = ExecutionWindow(startDateTime, endDateTime);

        val job = Job(1, "Emitir notas", LocalDateTime.of(2021, 10, 11, 12 ,0 , 0), Duration.ofMinutes(481));
        assertThrows(MaxDurationException::class.java) { service.validate(executionWindow, job) };
    }

    @Test
    fun deveLancarExceptionComJobForaDaJanela() {
        val service = SchedulerJobService();

        val startDateTime = LocalDateTime.of(2021, 10, 11, 8, 0, 0);
        val endDateTime = LocalDateTime.of(2021, 10, 11, 12, 0, 0);
        val executionWindow = ExecutionWindow(startDateTime, endDateTime);

        val jobBeforeExecutionWindow = Job(1, "Emitir notas", LocalDateTime.of(2021, 10, 11, 7, 59 , 59), Duration.ofHours(1));

        assertThrows(JobOutOfExecutionWindowException::class.java) { service.validate(executionWindow, jobBeforeExecutionWindow) };

        val jobAfterExecutionWindow = Job(1, "Emitir notas", LocalDateTime.of(2021, 10, 11, 12, 0 , 1), Duration.ofHours(1));
        assertThrows(JobOutOfExecutionWindowException::class.java) { service.validate(executionWindow, jobAfterExecutionWindow) };
    }

    @Test
    fun deveLancarExceptionComJobComDataMaximaEEstimativaIncompativeis() {
        val service = SchedulerJobService();

        val startDateTime = LocalDateTime.of(2021, 10, 10, 8, 0, 0);
        val endDateTime = LocalDateTime.of(2021, 10, 11, 12, 0, 0);
        val executionWindow = ExecutionWindow(startDateTime, endDateTime);

        val job = Job(1, "Emitir notas", LocalDateTime.of(2021, 10, 10, 10 ,0 , 0), Duration.ofMinutes(121));
        assertThrows(InvalidDurationJobException::class.java) { service.validate(executionWindow, job) };
    }
}
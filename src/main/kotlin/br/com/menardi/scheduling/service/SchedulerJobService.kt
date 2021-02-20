package br.com.menardi.scheduling.service

import br.com.menardi.scheduling.exceptions.InvalidDurationJobException
import br.com.menardi.scheduling.exceptions.JobOutOfExecutionWindowException
import br.com.menardi.scheduling.exceptions.MaxDurationException
import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
    import java.time.Duration

class SchedulerJobService {
    private val MAX_DURATION = Duration.ofHours(8);

    fun validate(executionWindow: ExecutionWindow, job: Job) {
        validateMaxDuration(job)
        validateExecutionWindow(executionWindow, job)
        validateDurationJob(executionWindow, job)
    }

    private fun validateMaxDuration(job: Job) {
        if (job.estimatedDuration > MAX_DURATION) {
            val estimatedDurationFormated = "${job.estimatedDuration.toHoursPart()}:${job.estimatedDuration.toMinutesPart()}:${job.estimatedDuration.toSecondsPart()}";
            val exceptionMessage = "Tempo estimado de $estimatedDurationFormated da job \"${job.description}\" é maior que 8 horas.";
            throw MaxDurationException(exceptionMessage);
        }
    }

    private fun validateExecutionWindow(executionWindow: ExecutionWindow, job: Job) {
        if (job.maxDateTimeToFinish.isBefore(executionWindow.start) || job.maxDateTimeToFinish.isAfter(executionWindow.end)) {
            val exceptionMessage = "A job \"${job.description}\" está fora da janela de execução.";
            throw JobOutOfExecutionWindowException(exceptionMessage);
        }
    }

    private fun validateDurationJob(executionWindow: ExecutionWindow, job: Job) {
        if (executionWindow.start.plus(job.estimatedDuration).isAfter(job.maxDateTimeToFinish)) {
            val exceptionMessage = "Não é possível executar a job \"${job.description}\" dentro da janela de execução no prazo máximo.";
            throw InvalidDurationJobException(exceptionMessage);
        }
    }
}
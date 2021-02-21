package br.com.menardi.scheduling.jobValidators

import br.com.menardi.scheduling.exceptions.JobOutOfExecutionWindowException
import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import java.time.Duration

class ExecutionWindowJobValidator(private val executionWindow: ExecutionWindow) : JobValidator {
    override fun validate(job: Job) {
        val windowDuration = Duration.between(executionWindow.start, executionWindow.end)
        if (job.maxDateTimeToFinish.isBefore(executionWindow.start) || job.estimatedDuration > windowDuration) {
            val exceptionMessage = "A job \"${job.description}\" está fora da janela de execução."
            throw JobOutOfExecutionWindowException(exceptionMessage)
        }
    }

}
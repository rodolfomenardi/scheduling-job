package br.com.menardi.scheduling.queueValidators

import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import java.time.Duration

class MaxDateTimeToFinishJobQueueValidator(private val executionWindow: ExecutionWindow) : ExecutionQueueValidator {
    override fun validate(currentDuration: Duration, job: Job): Boolean {
        val newDuration = currentDuration.plus(job.estimatedDuration)
        val estimatedEndExecution = executionWindow.start.plus(newDuration)

        return !estimatedEndExecution.isAfter(job.maxDateTimeToFinish)
    }
}
package br.com.menardi.scheduling.queueValidators

import br.com.menardi.scheduling.model.Job
import java.time.Duration

class MaxDurationQueueValidator(private val maxDuration: Duration) : ExecutionQueueValidator {
    override fun validate(currentDuration: Duration, job: Job): Boolean {
        val newDuration = currentDuration.plus(job.estimatedDuration)
        return newDuration <= maxDuration
    }
}
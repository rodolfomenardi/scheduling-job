package br.com.menardi.scheduling.queueValidators

import br.com.menardi.scheduling.model.Job
import java.time.Duration

interface ExecutionQueueValidator {
    fun validate(currentDuration: Duration, job: Job): Boolean
}
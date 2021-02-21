package br.com.menardi.scheduling.jobValidators

import br.com.menardi.scheduling.model.Job

interface JobValidator {
    fun validate(job: Job)
}
package br.com.menardi.scheduling.jobValidators

import br.com.menardi.scheduling.exceptions.MaxDurationException
import br.com.menardi.scheduling.extensions.toDisplay
import br.com.menardi.scheduling.model.Job
import java.time.Duration

class MaxDurationJobValidator(private val maxDuration: Duration) : JobValidator {
    override fun validate(job: Job) {
        if (job.estimatedDuration > maxDuration) {
            val estimatedDurationFormated = job.estimatedDuration.toDisplay()
            val exceptionMessage =
                "Tempo estimado de $estimatedDurationFormated da job \"${job.description}\" é maior que 8 horas."
            throw MaxDurationException(exceptionMessage)
        }
    }
}
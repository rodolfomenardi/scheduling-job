package br.com.menardi.scheduling.jobValidators

import br.com.menardi.scheduling.exceptions.InvalidDurationJobException
import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job

class JobDurationJobValidator(private val executionWindow: ExecutionWindow) : JobValidator {
    override fun validate(job: Job) {
        if (executionWindow.start.plus(job.estimatedDuration).isAfter(job.maxDateTimeToFinish)) {
            val exceptionMessage =
                "Não é possível executar a job dentro da janela de execução considerando a duração e o prazo maximo para finalizar a job."
            throw InvalidDurationJobException(exceptionMessage)
        }
    }
}
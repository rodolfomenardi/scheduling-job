package br.com.menardi.scheduling.service

import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
    import java.time.Duration

class SchedulerJobService {
    private val MAX_TIME = Duration.ofHours(8);

    fun validar(executionWindow: ExecutionWindow, job: Job) {
        if (job.estimatedTimeToFinish > MAX_TIME) {
            val estimatedDurationFormated = "${job.estimatedTimeToFinish.toHoursPart()}:${job.estimatedTimeToFinish.toMinutesPart()}:${job.estimatedTimeToFinish.toSecondsPart()}";
            throw RuntimeException("Tempo estimado de ${estimatedDurationFormated} da job \"${job.description}\" é maior que 8 horas.");
        }

        if (job.maxDateToFinish.isBefore(executionWindow.start) || job.maxDateToFinish.isAfter(executionWindow.end)) {
            throw RuntimeException("A job \"${job.description}\" está fora da janela de execução.");
        }

        if (executionWindow.start.plus(job.estimatedTimeToFinish).isAfter(job.maxDateToFinish)) {
            throw RuntimeException("Não é possível executar a job \"${job.description}\" dentro da janela de execução no prazo máximo.");
        }
    }
}
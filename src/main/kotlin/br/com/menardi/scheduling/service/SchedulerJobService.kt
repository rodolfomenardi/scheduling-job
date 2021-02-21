package br.com.menardi.scheduling.service

import br.com.menardi.scheduling.jobValidators.JobDurationJobValidator
import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import br.com.menardi.scheduling.queueValidators.ExecutionWindowQueueValidator
import br.com.menardi.scheduling.queueValidators.MaxDateTimeToFinishJobQueueValidator
import br.com.menardi.scheduling.queueValidators.MaxDurationQueueValidator
import java.time.Duration

class SchedulerJobService(private val executionWindow: ExecutionWindow) {
    private val maxDuration = Duration.ofHours(8)

    fun getListsToExecution(jobs: List<Job>): List<List<Job>> {
        jobsValidate(jobs)

        val jobsOrdered = jobs.sortedBy { it.maxDateTimeToFinish }.toMutableList()

        val listsToExecution = mutableListOf<List<Job>>()

        while (jobsOrdered.isNotEmpty()) {
            val actualJobs = getNextExecutionList(jobsOrdered)
            jobsOrdered.removeIf { jobOrdered ->
                actualJobs.stream().anyMatch { actualJob -> actualJob.id == jobOrdered.id }
            }
            listsToExecution.add(actualJobs)
        }

        return listsToExecution.toList()
    }

    private fun getNextExecutionList(jobsOrdered: List<Job>): List<Job> {
        var totalDuration = Duration.ZERO
        val actualJobs = mutableListOf<Job>()

        jobsOrdered.forEach { job ->
            if (queueValidate(totalDuration, job)) {
                totalDuration = totalDuration.plus(job.estimatedDuration)
                actualJobs.add(job)
            }
        }

        return actualJobs.toList()
    }

    private fun jobsValidate(jobs: List<Job>) {
        val validations = listOf(
            br.com.menardi.scheduling.jobValidators.MaxDurationJobValidator(maxDuration),
            br.com.menardi.scheduling.jobValidators.ExecutionWindowJobValidator(executionWindow),
            JobDurationJobValidator(executionWindow)
        )

        jobs.forEach { job ->
            validations.forEach { validator -> validator.validate(job) }
        }
    }

    private fun queueValidate(newDuration: Duration, job: Job): Boolean {
        val rulesExecution = listOf(
            MaxDurationQueueValidator(maxDuration),
            MaxDateTimeToFinishJobQueueValidator(executionWindow),
            ExecutionWindowQueueValidator(executionWindow)
        )

        return !rulesExecution.stream().anyMatch { validator -> !validator.validate(newDuration, job) }
    }
}
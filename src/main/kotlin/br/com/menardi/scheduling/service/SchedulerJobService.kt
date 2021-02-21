package br.com.menardi.scheduling.service

import br.com.menardi.scheduling.jobValidators.ExecutionWindowJobValidator
import br.com.menardi.scheduling.jobValidators.JobDurationJobValidator
import br.com.menardi.scheduling.jobValidators.MaxDurationJobValidator
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

        val listsToExecution = mutableListOf<List<Job>>()

        val jobsOrdered = jobs.sortedBy { it.maxDateTimeToFinish }.toMutableList()
        while (jobsOrdered.isNotEmpty()) {
            val newJobsList = getNewExecutionList(jobsOrdered.toList())
            jobsOrdered.removeIf { jobOrdered ->
                newJobsList.stream().anyMatch { actualJob -> actualJob.id == jobOrdered.id }
            }
            listsToExecution.add(newJobsList)
        }

        return listsToExecution.toList()
    }

    private fun getNewExecutionList(jobs: List<Job>): List<Job> {
        var totalDuration = Duration.ZERO
        val actualJobs = mutableListOf<Job>()

        jobs.forEach { job ->
            if (canAddJob(totalDuration, job)) {
                totalDuration = totalDuration.plus(job.estimatedDuration)
                actualJobs.add(job)
            }
        }

        return actualJobs.toList()
    }

    private fun jobsValidate(jobs: List<Job>) {
        val validations = listOf(
            MaxDurationJobValidator(maxDuration),
            ExecutionWindowJobValidator(executionWindow),
            JobDurationJobValidator(executionWindow)
        )

        jobs.forEach { job ->
            validations.forEach { validator -> validator.validate(job) }
        }
    }

    private fun canAddJob(currentDuration: Duration, job: Job): Boolean {
        val rulesExecution = listOf(
            MaxDurationQueueValidator(maxDuration),
            MaxDateTimeToFinishJobQueueValidator(executionWindow),
            ExecutionWindowQueueValidator(executionWindow)
        )

        return !rulesExecution.stream().anyMatch { validator -> !validator.validate(currentDuration, job) }
    }
}
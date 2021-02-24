package br.com.menardi.scheduling

import br.com.menardi.scheduling.model.ExecutionWindow
import br.com.menardi.scheduling.model.Job
import br.com.menardi.scheduling.service.JsonParserService
import br.com.menardi.scheduling.service.SchedulerJobService
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.Reader

private val jsonParserService = JsonParserService()

fun main(args: Array<String>) {
    if (args.size < 3) {
        throw IllegalArgumentException("Arguments Invalids")
    }

    val executionWindowFilePath = args[0]
    val jobsFilePath = args[1]
    val jobsResultFilePath = args[2]
    Application().run(executionWindowFilePath, jobsFilePath, jobsResultFilePath)
}

class Application {
    fun run(executionWindowFilePath: String, jobsFilePath: String, jobsResultFilePath: String) {
        val executionWindowReader = FileReader(executionWindowFilePath, Charsets.UTF_8)
        val jobsReader = FileReader(jobsFilePath, Charsets.UTF_8)

        val executionWindow = jsonParserService.parseFromReader<ExecutionWindow>(executionWindowReader)
        val jobs = jsonParserService.parseArrayFromReader<Job>(jobsReader)

        val executionJobs = SchedulerJobService(executionWindow).getListsToExecution(jobs)

        val jsonResult = jsonParserService.toJsonString(executionJobs)

        println(jsonResult)

        val processedFile = File(jobsResultFilePath)
        processedFile.writeText(jsonResult)
    }
}
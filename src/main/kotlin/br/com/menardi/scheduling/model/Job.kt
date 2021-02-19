package br.com.menardi.scheduling.model

import java.time.Duration
import java.time.LocalDateTime

data class Job(
    val id: Long,
    val description: String,
    val maxDateToFinish: LocalDateTime,
    val estimatedTimeToFinish: Duration
)
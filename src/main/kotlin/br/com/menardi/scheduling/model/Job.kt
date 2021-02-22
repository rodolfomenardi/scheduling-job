package br.com.menardi.scheduling.model

import br.com.menardi.scheduling.annotations.JsonDuration
import br.com.menardi.scheduling.annotations.JsonLocalDateTime
import java.time.Duration
import java.time.LocalDateTime


data class Job(
    val id: Long,
    val description: String,
    @JsonLocalDateTime
    val maxDateTimeToFinish: LocalDateTime,
    @JsonDuration
    val estimatedDuration: Duration
)
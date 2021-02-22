package br.com.menardi.scheduling.model

import br.com.menardi.scheduling.annotations.JsonLocalDateTime
import com.beust.klaxon.Json
import java.time.LocalDateTime

data class ExecutionWindow(
    @JsonLocalDateTime
    val start: LocalDateTime,
    @JsonLocalDateTime
    val end: LocalDateTime
)

package br.com.menardi.scheduling.model

import java.time.LocalDateTime

data class ExecutionWindow(
    val start: LocalDateTime,
    val end: LocalDateTime)

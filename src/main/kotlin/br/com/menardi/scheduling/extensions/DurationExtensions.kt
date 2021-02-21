package br.com.menardi.scheduling.extensions

import java.time.Duration

fun Duration.toDisplay(): String {
    val hours = padStart(this.toHoursPart())
    val minutes = padStart(this.toMinutesPart())
    val seconds = padStart(this.toSecondsPart())

    return "$hours:$minutes:$seconds"
}

private fun padStart(valor: Int): String {
    return valor.toString().padStart(2, '0')
}
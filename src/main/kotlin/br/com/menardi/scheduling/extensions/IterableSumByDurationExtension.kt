package br.com.menardi.scheduling.extensions

import java.time.Duration

fun <T> Iterable<T>.sumByDuration(selector: (T) -> Duration): Duration {
    var sum = Duration.ZERO
    for (element in this) {
        sum = sum.plus(selector(element))
    }

    return sum
}
@file:OptIn(ExperimentalTime::class)

package com.rollinup.common.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object Utils {
    fun String.toLocalDateTime(
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): LocalDateTime {
        return try {
            Instant.parse(this).toLocalDateTime(timeZone)
        } catch (e: IllegalArgumentException) {
            LocalDateTime.now()
        }
    }

    fun String.toLocalTime(): LocalTime =
        try {
            LocalTime.parse(this)
        } catch (e: IllegalArgumentException) {
            LocalTime.fromSecondOfDay(0)
        }

    fun LocalDateTime.toEpochMillis(
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): Long = this.toInstant(timeZone).toEpochMilliseconds()

    fun LocalDate.Companion.now(
        clock: Clock = Clock.System,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): LocalDate = clock.todayIn(timeZone)

    fun LocalTime.Companion.now(
        clock: Clock = Clock.System,
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): LocalTime = clock.now().toLocalDateTime(timeZone).time

    fun LocalDateTime.Companion.now(
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): LocalDateTime = Clock.System.now().toLocalDateTime(timeZone)

    fun Long.toLocalDateTime(
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): LocalDateTime =
        Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone)

    fun Long.toLocalDate(
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): LocalDate =
        toLocalDateTime(timeZone).date

    fun LocalDate.toEpochMilli(
        timeZone: TimeZone = TimeZone.currentSystemDefault(),
    ): Long =
        LocalDateTime(
            date = this,
            time = LocalTime.fromSecondOfDay(1)
        )
            .toInstant(timeZone)
            .toEpochMilliseconds()

    fun LocalDate.toFormattedString(): String {
        val day = this.day
        val month = this.month.name.take(3)
        val year = this.year
        return "$day $month $year"
    }

}
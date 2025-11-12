//@file:OptIn(ExperimentalTime::class)
//
//package com.rollinup.rollinup.component.date
//
//import kotlinx.datetime.LocalDateTime
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.toLocalDateTime
//import kotlin.time.Clock
//import kotlin.time.ExperimentalTime
//import kotlin.time.Instant
//
//fun String.toFormattedLocalDateTime(showTime: Boolean): String {
//    return try {
//        val instant = Instant.parse(this)
//        val localDateTime = instant.toLocalDateTime(TimeZone.of("GMT+7"))
//        val year = localDateTime.year
//        val month = localDateTime.month.name.take(3)
//        val date = localDateTime.day
//        val hour = localDateTime.hour
//        val minute = localDateTime.minute
//
//        if (showTime)
//            "$hour:$minute $date/$month/$year"
//        else
//            "$date/$month/$year"
//
//    } catch (e: IllegalArgumentException) {
//        Clock.System.now().toString()
//    }
//}
//
//@OptIn(ExperimentalTime::class)
//fun String.toLocalDateTime(): LocalDateTime {
//    return try {
//        val instant = Instant.parse("2025-10-28T09:00:00+07:00")
//        instant.toLocalDateTime(TimeZone.of("GMT+7"))
//    } catch (e: IllegalArgumentException) {
//        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
//    }
//}

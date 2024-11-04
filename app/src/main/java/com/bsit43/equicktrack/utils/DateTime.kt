package com.bsit43.equicktrack.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId


fun currentDate() : LocalDateTime {
    return LocalDateTime.now(ZoneId.of("Asia/Manila"))
}

fun selectedTimeIntoLocalDateTime(selectedHour : Int, selectedMinute : Int, amPm: String) : LocalDateTime {

    val hour24 = when {
        amPm == "PM" && selectedHour != 12 -> selectedHour + 12
        amPm == "AM" && selectedHour == 12 -> 0
        else -> selectedHour
    }

//    val currentDate = LocalDate.now(ZoneId.of("Asia/Manila"))
    val currentDate = LocalDate.now()
    val selectedTime = LocalTime.of(hour24, selectedMinute)

    return LocalDateTime.of(currentDate, selectedTime)

}
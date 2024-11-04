package com.bsit43.equicktrack.dto

import java.time.LocalDateTime

data class BorrowRequest(
    val userId : Long,
    val equipmentId : Long,
    val purpose : String?,
    val borrowDate : LocalDateTime,
    val returnDate : LocalDateTime,
)
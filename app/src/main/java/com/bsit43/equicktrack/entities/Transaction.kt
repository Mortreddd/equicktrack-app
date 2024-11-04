package com.bsit43.equicktrack.entities

data class Transaction(
    val id : Long,
    val userId : Long?,
    val equipmentId : Long?,
    var purpose : String?,
    var borrowDate : String,
    var returnData : String,
    var createdAt : String,
    var updatedAt : String,
    var returnedAt: String?,
    var remark: Remark?,
    var notifiedAt : String?,
    var conditionImage : String?,
    var equipment : Equipment?,
    val user : User?

)
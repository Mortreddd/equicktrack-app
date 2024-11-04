package com.bsit43.equicktrack.entities

data class Equipment(
    val id : Long,
    var name : String,
    var description: String? = null,
    var equipmentImage : String,
    var qrcodeImage : String,
    var remark : Remark = Remark.GOOD_CONDITION,
    var available : Boolean,
    var createdAt : String,
    var updatedAt : String,
    val transactions : List<Transaction>
)
package com.bsit43.equicktrack.entities

data class User(
    val id : Long,
    val googleUuid : String?,
    var fullName : String,
    var email : String,
    var photoUrl : String?,
    var contactNumber : String,
    var password : String,
    var emailVerifiedAt : String?,
    var createdAt : String?,
    var updatedAt : String?,

    var transactions : List<Transaction>,
    var authorities : Set<Authority>?,
    val roles : Set<Role>?

)
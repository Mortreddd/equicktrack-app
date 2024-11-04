package com.bsit43.equicktrack.entities

enum class ERole(val value : String) {
    SUPER_ADMIN("Super Admin"),
    ADMIN("Admin"),
    PROFESSOR("Teacher"),
    STUDENT("Student")

}

data class Role(val id : Int, val name : ERole)
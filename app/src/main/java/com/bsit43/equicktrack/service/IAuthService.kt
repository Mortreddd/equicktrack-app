package com.bsit43.equicktrack.service

import com.bsit43.equicktrack.auth.LoginRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface IAuthService {

    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<String>

}
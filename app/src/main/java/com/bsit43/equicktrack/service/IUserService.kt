package com.bsit43.equicktrack.service

import com.bsit43.equicktrack.entities.Transaction
import com.bsit43.equicktrack.entities.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface IUserService {
    @Headers("Content-Type: application/json")
    @GET("users/me")
    fun getUser(
        @Header("Authorization") authToken : String?
    ) : Call<User>

    @Headers("Content-Type: application/json")
    @GET("users/{userId}/transactions")
    fun getTransactionByUserId(
        @Path("userId") userId : Long,
        @Header("Authorization") authToken : String?
    ) : Call<List<Transaction>>
}
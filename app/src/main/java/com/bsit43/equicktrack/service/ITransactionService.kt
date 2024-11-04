package com.bsit43.equicktrack.service

import com.bsit43.equicktrack.dto.BorrowRequest
import com.bsit43.equicktrack.entities.Transaction
import com.bsit43.equicktrack.utils.Paginate
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ITransactionService {

    @Headers("Content-Type: application/json")
    @GET("transactions")
    fun getTransactions(
        @Header("Authorization") authToken : String?,
        @Query("pageNo") pageNo : Int = 0,
        @Query("pageSize") pageSize : Int = 10,
        @Query("search") search : String? = null,

    ) : Call<Paginate<List<Transaction>>>

    @Headers("Content-Type: application/json")
    @GET("transactions/{id}")
    fun getTransactionById(
        equipmentId : Long
    ) : Call<Transaction>

    @Headers("Content-Type: application/json")
    @GET("equipments/{equipmentId}/transactions")
    fun getTransactionsByEquipmentId(
        @Path("equipmentId") equipmentId : Long
    ) : Call<List<Transaction>>

    @Headers("Content-Type: application/json")
    @GET("users/{userId}/transactions")
    fun getTransactionsByUserId(
        @Path("userId") userId : Long
    ) : Call<List<Transaction>>

    @Headers("Content-Type: application/json")
    @POST("transactions/borrow")
    fun createBorrowTransaction(
        @Header("Authorization") authToken : String,
        @Body borrowRequest : BorrowRequest
    ) : Call<Transaction>

    @Headers("Content-Type: multipart/form-data")
    @PATCH("transactions/return")
    fun createReturnTransaction(

    )
}
package com.bsit43.equicktrack.service

import com.bsit43.equicktrack.entities.Equipment
import com.bsit43.equicktrack.entities.Transaction
import com.bsit43.equicktrack.utils.Paginate
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface IEquipmentService {

    @GET("equipments")
    fun getEquipments(
        @Header("Authorization") authToken : String?,
        @Query("pageNo") pageNo : Int = 0,
        @Query("pageSize") pageSize : Int = 10,
        @Query("search") search : String? = null
    ) : Call<Paginate<List<Equipment>>>

    @GET("equipments/{equipmentId}")
    fun getEquipmentById(
        @Header("Authorization") authToken: String,
        @Path("equipmentId") equipmentId : Long
    ) : Call<Equipment>

    @GET("equipments/{equipmentId}/transactions")
    fun getTransactionsByEquipmentId(
        @Header("Authorization") authToken: String,
        @Path("equipmentId") equipmentId : Long
    ) : Call<List<Transaction>>

    @GET("equipments/qrcode/{qrcodeData}")
    fun getEquipmentByQrcode(
        @Header("Authorization") authToken : String,
        @Path("qrcodeData") qrcodeData : String
    ) : Call<Equipment>
}
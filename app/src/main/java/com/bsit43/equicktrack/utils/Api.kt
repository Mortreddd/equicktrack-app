package com.bsit43.equicktrack

import retrofit2.Retrofit
import com.bsit43.equicktrack.service.IAuthService
import com.bsit43.equicktrack.service.IEquipmentService
import com.bsit43.equicktrack.service.ITransactionService
import com.bsit43.equicktrack.service.IUserService
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitClient {
    private val apiBaseUrl : String = "http://10.0.2.2:8080/api/v1/"

    val api by lazy {
        Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiUser: IUserService by lazy {
        Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IUserService::class.java)
    }

    val apiEquipment: IEquipmentService by lazy {
        Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IEquipmentService::class.java)
    }

    val apiTransactions: ITransactionService by lazy {
        Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITransactionService::class.java)
    }

    val apiAuth: IAuthService by lazy {
        Retrofit.Builder()
            .baseUrl(apiBaseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IAuthService::class.java)
    }

}

package com.bsit43.equicktrack.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bsit43.equicktrack.RetrofitClient
import com.bsit43.equicktrack.dto.BorrowRequest
import com.bsit43.equicktrack.entities.Equipment
import com.bsit43.equicktrack.entities.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class HomeViewModel : ViewModel() {
    private var _userTransactions = MutableLiveData<List<Transaction>?>()
    private val _selectedEquipment = MutableLiveData<Equipment?>()
    private var _createdTransaction = MutableLiveData<Transaction?>()
    val userTransactions : LiveData<List<Transaction>?> = _userTransactions
    val selectedEquipment : LiveData<Equipment?> = _selectedEquipment
    val createdTransaction : LiveData<Transaction?> = _createdTransaction

    fun getUserTransaction(userId : Long, authToken : String? = null) : Unit {
        val call = RetrofitClient.apiUser.getTransactionByUserId(userId = userId, authToken  = "Bearer $authToken")
        call.enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(request: Call<List<Transaction>>, response: Response<List<Transaction>>) {
                Log.d("User Transaction Response", "${response.body()}")
                _userTransactions.value = if(response.isSuccessful) response.body() else null
            }

            override fun onFailure(request: Call<List<Transaction>>, exception: Throwable) {
                exception.message?.let { Log.d("User Transaction Response", it) }
            }

        })
    }

    fun createBorrowRequest(
        authToken : String,
        userId : Long,
        equipmentId: Long,
        purpose : String?,
        borrowDate: LocalDateTime,
        returnDate : LocalDateTime
    ) {

        val call = RetrofitClient.apiTransactions.createBorrowTransaction(
            authToken = "Bearer $authToken", borrowRequest = BorrowRequest(
                userId = userId,
                equipmentId = equipmentId,
                purpose = purpose,
                borrowDate = borrowDate,
                returnDate = returnDate
            )
        )

        call.enqueue(object: Callback<Transaction> {
            override fun onResponse(request: Call<Transaction>, response: Response<Transaction>) {
                if(response.isSuccessful) {
                    _createdTransaction.value = response.body()
                } else {
                    _createdTransaction.value = null
                }
            }

            override fun onFailure(request: Call<Transaction>, exception: Throwable) {
                exception.message?.let { Log.d("Borrow Response", it) }
                _createdTransaction.value = null
            }
        })
    }

    fun getEquipmentByQrcode(authToken : String, qrcodeData : String) : Unit {
        val call = RetrofitClient.apiEquipment.getEquipmentByQrcode(authToken = "Bearer $authToken", qrcodeData = qrcodeData)

        call.enqueue(object : Callback<Equipment> {
            override fun onResponse(request: Call<Equipment>, response: Response<Equipment>) {
                Log.d("Equipment By Qrcode Response", "${response.body()}")
                _selectedEquipment.value = if(response.isSuccessful) response.body()else null
            }

            override fun onFailure(request: Call<Equipment>, exception: Throwable) {
                Log.d("Equipment By Qrcode Error", "${exception.message}")
                _selectedEquipment.value = null
            }

        })

    }
}
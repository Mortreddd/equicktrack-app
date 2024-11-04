package com.bsit43.equicktrack.ui.equipments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bsit43.equicktrack.entities.Equipment
import com.bsit43.equicktrack.RetrofitClient
import com.bsit43.equicktrack.utils.Paginate
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EquipmentViewModel : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    private val _equipments = MutableLiveData<List<Equipment>?>()
    private val _search = MutableLiveData<String?>()

    val loading : LiveData<Boolean> = _loading
    val equipments: LiveData<List<Equipment>?> = _equipments
    val search : LiveData<String?> = _search

    private var searchJob : Job? = null
    fun getEquipments(authToken : String?) : Unit {

        val call = RetrofitClient.apiEquipment.getEquipments(authToken =  "Bearer $authToken", search = search.value)
        call.enqueue(object : Callback<Paginate<List<Equipment>>> {
            override fun onResponse(call: Call<Paginate<List<Equipment>>>, response: Response<Paginate<List<Equipment>>>) {
                if(response.isSuccessful) {
                    _equipments.value = response.body()?.content
                    _loading.value = false
                } else {
                    Log.e("EquipmentViewModel", "Response Error: ${response.code()}")
                    _equipments.value = null
                    _loading.value = false
                }
            }

            override fun onFailure(p0: Call<Paginate<List<Equipment>>>, p1: Throwable) {
                Log.e("EquipmentViewModel", "Fetch Failure: ${p1.message}")
                _loading.value = false
                _equipments.value = null
            }
        })
    }

    fun setSearch(value: String) : Unit {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(500)
            _search.value = value
        }
    }
}
package com.bsit43.equicktrack.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bsit43.equicktrack.RetrofitClient
import com.bsit43.equicktrack.entities.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val _auth = MutableLiveData<Auth>()
    val auth: LiveData<Auth> = _auth
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    init {
         _auth.value = Auth(null, null)
    }

    fun login(loginRequest: LoginRequest) {
        _loading.value = true
        val call = RetrofitClient.apiAuth.login(loginRequest)
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d("Login Response", response.toString())
                if (response.isSuccessful && response.body() != null) {
                    _auth.value = Auth(null, authToken = response.body())
                    ("${_auth.value?.authToken}" ?: null)?.let { Log.d("Auth Value", it) }
                } else {
                    Log.d("Login Response Error", response.errorBody()?.string() ?: "Unknown error")
                    _auth.value = Auth(null, null)
                }
                _loading.value = false
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d("Login Error", t.message ?: "Unknown error")
                _auth.value = Auth(null, null)
                _loading.value = false
            }
        })
    }


    fun getUser() : Unit {
        _loading.value = true
        val call = RetrofitClient.apiUser.getUser(authToken = "Bearer ${auth.value?.authToken}")
        Log.d("Auth Token in getUser", "Bearer ${auth.value?.authToken}")
        call.enqueue(object : Callback<User> {
            override fun onResponse(request: Call<User>, response: Response<User>) {
                Log.d("User Response", response.body().toString())
                setAuth(token = auth.value?.authToken, user = response.body())
                _loading.value = false
            }

            override fun onFailure(p0: Call<User>, exception: Throwable) {
                Log.d("User Error", exception.message ?: "Unknown error")
                _auth.value?.user = null
                _loading.value = false
            }
        })

    }

    fun setAuth(token : String? = null, user : User? = null) : Unit {
        val currentAuth = _auth.value ?: Auth(user =  user, authToken = token)
        _auth.value = currentAuth.copy(authToken = token, user = user)
    }

    fun logout() : Unit {
        _auth.value = Auth(null, null)
    }


}
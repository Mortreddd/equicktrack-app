package com.bsit43.equicktrack

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bsit43.equicktrack.ui.HomeLayoutActivity
import com.bsit43.equicktrack.ui.auth.LoginActivity

class EquicktrackApplication : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.landing_layout)
        val sharedPreferences: SharedPreferences = getSharedPreferences("appRefs", MODE_PRIVATE)
        val authToken: String? = sharedPreferences.getString("authToken", null)
        Handler(Looper.getMainLooper()).postDelayed({

            val intent : Intent = if (authToken.isNullOrEmpty())
                Intent(this@EquicktrackApplication, LoginActivity::class.java)
            else
                Intent(this@EquicktrackApplication, HomeLayoutActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}


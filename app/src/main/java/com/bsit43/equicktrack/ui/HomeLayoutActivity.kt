package com.bsit43.equicktrack.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bsit43.equicktrack.R
import com.bsit43.equicktrack.auth.AuthViewModel
import com.bsit43.equicktrack.ui.auth.LoginActivity
import com.bsit43.equicktrack.ui.equipments.EquipmentFragment
import com.bsit43.equicktrack.ui.home.HomeFragment
import com.bsit43.equicktrack.ui.settings.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeLayoutActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this@HomeLayoutActivity)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout)
        initViews()

        val sharedPreferences: SharedPreferences = getSharedPreferences("appRefs", MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", null)

        if (!authToken.isNullOrEmpty()) {
            authViewModel.setAuth(token = authToken)
            authViewModel.getUser()
        }

        authViewModel.auth.observe(this) { auth ->
            if (auth.authToken != null && auth.user == null) {
                authViewModel.getUser()  // Trigger getUser if the user is missing
            } else if (auth.authToken.isNullOrEmpty()) {
                // Redirect to LoginActivity if the token is missing
                val loginIntent = Intent(this@HomeLayoutActivity, LoginActivity::class.java)
                startActivity(loginIntent)
                finish()
            }
        }


        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomeFragment())
                R.id.equipments -> replaceFragment(EquipmentFragment())
                R.id.settings -> replaceFragment(SettingFragment())
            }
            true
        }

        replaceFragment(HomeFragment())
    }

    private fun initViews(): Unit {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
    }

    private fun replaceFragment(fragment: Fragment): Unit {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()
    }
}

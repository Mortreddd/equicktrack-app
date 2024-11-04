package com.bsit43.equicktrack.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bsit43.equicktrack.R
import com.bsit43.equicktrack.RetrofitClient
import com.bsit43.equicktrack.auth.AuthViewModel
import com.bsit43.equicktrack.auth.LoginRequest
import com.bsit43.equicktrack.ui.HomeLayoutActivity
import com.google.android.material.button.MaterialButton

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText : EditText
    private lateinit var passwordEditText : EditText
    private lateinit var loginButton : MaterialButton
    private lateinit var loginRequest : LoginRequest
    private val authViewModel : AuthViewModel by lazy {
        ViewModelProvider(this@LoginActivity)[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        initViews()

        val sharedPreferences : SharedPreferences = getSharedPreferences("appRefs", MODE_PRIVATE)

        authViewModel.auth.observe(this) { auth ->
            auth.authToken?.let {
                // Save token to SharedPreferences and navigate to HomeActivity
                sharedPreferences.edit().putString("authToken", it).apply()
                val homeIntent = Intent(this@LoginActivity, HomeLayoutActivity::class.java)
                startActivity(homeIntent)
                finish()
            } ?: run {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe loading to manage button state
        authViewModel.loading.observe(this) {
            loginButton.isEnabled = !it
        }

        // Handle button click to trigger login
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            authViewModel.login(LoginRequest(email = email, password = password))
        }
    }

    private fun initViews() : Unit {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
    }


}
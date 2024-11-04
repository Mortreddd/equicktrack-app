package com.bsit43.equicktrack.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bsit43.equicktrack.R
import com.bsit43.equicktrack.auth.AuthViewModel
import com.bsit43.equicktrack.ui.auth.LoginActivity
import com.google.android.material.button.MaterialButton

class SettingFragment : Fragment() {

    private lateinit var logoutButton : MaterialButton
    private val authViewModel : AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logoutButton = view.findViewById(R.id.logoutButton)
        val sharedPreferences = view.context.getSharedPreferences("appRefs", Context.MODE_PRIVATE)

        logoutButton.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            authViewModel.logout()
            val loginIntent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(loginIntent)
            requireActivity().finish()
        }
    }
}
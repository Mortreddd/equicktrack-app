package com.bsit43.equicktrack.ui.equipments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bsit43.equicktrack.R
import com.bsit43.equicktrack.auth.AuthViewModel

class EquipmentFragment : Fragment() {
    private val equipmentViewModel : EquipmentViewModel by lazy {
        ViewModelProvider(this@EquipmentFragment)[EquipmentViewModel::class.java]
    }
    private val authViewModel : AuthViewModel by activityViewModels()
    private lateinit var equipmentGridView: GridView
    private lateinit var progressBar : ProgressBar
    private lateinit var errorTextView : TextView
    private lateinit var searchEquipmentEditText : EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_equipment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)

        equipmentViewModel.getEquipments(authViewModel.auth.value?.authToken)
        equipmentViewModel.equipments.observe(viewLifecycleOwner) {
            Log.d("Equipments", "$it")
            if(it.isNullOrEmpty()) {
                onError()
                return@observe
            }

            val adapter = EquipmentCardAdapter(view.context, it)
            equipmentGridView.adapter = adapter
            onLoaded()
        }

        equipmentViewModel.loading.observe(viewLifecycleOwner) {
            if(it) {
                onLoading()
            }
        }

        equipmentViewModel.search.observe(viewLifecycleOwner) {
            equipmentViewModel.getEquipments(authViewModel.auth.value?.authToken)
        }

        searchEquipmentEditText.addTextChangedListener{
            if(it.isNullOrEmpty()) {
                return@addTextChangedListener
            }
            equipmentViewModel.setSearch(it.toString())
        }
    }

    private fun initViews(view: View) : Unit {
        equipmentGridView = view.findViewById(R.id.equipmentGridView)
        progressBar = view.findViewById(R.id.progressBar)
        errorTextView = view.findViewById(R.id.errorTextView)
        searchEquipmentEditText = view.findViewById(R.id.searchEquipmentEditText)
    }

    private fun onLoading() : Unit {
        progressBar.visibility = View.VISIBLE
        equipmentGridView.visibility = View.GONE
        errorTextView.visibility = View.GONE
    }

    private fun onLoaded() : Unit {
        progressBar.visibility = View.GONE
        equipmentGridView.visibility = View.VISIBLE
        errorTextView.visibility = View.GONE
    }

    private fun onError() : Unit {
        progressBar.visibility = View.GONE
        equipmentGridView.visibility = View.GONE
        errorTextView.visibility = View.VISIBLE
    }


}
package com.bsit43.equicktrack.ui.equipments


import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NavUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bsit43.equicktrack.R
import com.bsit43.equicktrack.RetrofitClient
import com.bsit43.equicktrack.entities.Equipment
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EquipmentDescriptionLayoutActivity : AppCompatActivity() {

    private lateinit var equipmentIdTextView : TextView
    private lateinit var equipmentNameTextView : TextView
    private lateinit var equipmentDescriptionTextView : TextView
    private lateinit var equipmentRemarkTextView : TextView
    private lateinit var equipmentImageView : ImageView
    private lateinit var loadingBar : ProgressBar
    private lateinit var errorMessage : TextView
    private val _equipment = MutableLiveData<Equipment>()
    private val equipment : LiveData<Equipment> = _equipment
    private val _loading = MutableLiveData<Boolean>()
    private val loading : LiveData<Boolean> = _loading


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.equipment_description_layout)
        initViews()
        val actionBar = supportActionBar

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Description"
        }

        val equipmentId : Long = intent.getLongExtra("equipmentId", 0)
        getEquipment(equipmentId)
        loading.observe(this) {
            if(it) {
                loadingEquipment()
            }
        }

        equipment.observe(this) {
            if(it != null) {
                showEquipment()
                equipmentIdTextView.text = it.id.toString()
                equipmentNameTextView.text = it.name
                equipmentDescriptionTextView.text = it.description
                equipmentRemarkTextView.text = it.remark.value
                Glide.with(this).load(it.equipmentImage).into(equipmentImageView)

            } else {
                showError()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }


    private fun initViews() : Unit {
        equipmentIdTextView = findViewById(R.id.equipmentId)
        equipmentNameTextView = findViewById(R.id.equipmentName)
        equipmentDescriptionTextView = findViewById(R.id.equipmentDescription)
        equipmentRemarkTextView = findViewById(R.id.equipmentRemark)
        equipmentImageView = findViewById(R.id.equipmentImage)
        loadingBar = findViewById(R.id.loading)
        errorMessage = findViewById(R.id.errorMessage)
    }

    private fun getEquipment(equipmentId: Long) : Unit {
        _loading.value = true
        val authToken : String? = getSharedPreferences("appRefs", MODE_PRIVATE).getString("authToken", null)

        if(authToken.isNullOrEmpty() || equipmentId == 0L) {
            showError()
            return
        }

        val call = RetrofitClient.apiEquipment.getEquipmentById(authToken = "Bearer $authToken", equipmentId = equipmentId)

        try {
            call.enqueue(object : Callback<Equipment> {
                override fun onResponse(request: Call<Equipment>, response: Response<Equipment>) {
                    if(response.isSuccessful) {
                        _equipment.value = response.body()
                        showEquipment()
                    } else {
                        showError()
                    }
                }

                override fun onFailure(request: Call<Equipment>, exception: Throwable) {
                    showError()
                    return
                }

            })
        } catch (e : Exception) {
            showError()
            return
        } finally {
            _loading.value = false
        }

    }


    private fun loadingEquipment() : Unit {
        loadingBar.visibility = View.VISIBLE
        errorMessage.visibility = View.GONE
        equipmentImageView.visibility = View.GONE
        equipmentIdTextView.visibility = View.GONE
        equipmentNameTextView.visibility = View.GONE
        equipmentDescriptionTextView.visibility = View.GONE
        equipmentRemarkTextView.visibility = View.GONE

    }

    private fun showEquipment() : Unit {
        loadingBar.visibility = View.GONE
        errorMessage.visibility = View.GONE
        equipmentImageView.visibility = View.VISIBLE
        equipmentIdTextView.visibility = View.VISIBLE
        equipmentNameTextView.visibility = View.VISIBLE
        equipmentDescriptionTextView.visibility = View.VISIBLE
        equipmentRemarkTextView.visibility = View.VISIBLE
    }

    private fun showError() : Unit {
        errorMessage.visibility = View.VISIBLE
        loadingBar.visibility = View.GONE
        equipmentImageView.visibility = View.GONE
        equipmentIdTextView.visibility = View.GONE
        equipmentNameTextView.visibility = View.GONE
        equipmentDescriptionTextView.visibility = View.GONE
        equipmentRemarkTextView.visibility = View.GONE
    }
}
package com.bsit43.equicktrack.ui.equipments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.window.Dialog
import com.bsit43.equicktrack.entities.Equipment
import com.bsit43.equicktrack.R
import com.bumptech.glide.Glide

class EquipmentCardAdapter(context: Context, equipments: List<Equipment?>?) :
    ArrayAdapter<Equipment?>(context, 0, equipments!!){


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var listItemView = convertView
        if ( listItemView == null ) {
            listItemView = LayoutInflater.from(context).inflate(R.layout.equipment_item, parent, false)
        }
        val currentEquipment: Equipment? = getItem(position)
        val equipmentNameView = listItemView!!.findViewById<TextView>(R.id.equipmentName)
        val equipmentImageView = listItemView.findViewById<ImageView>(R.id.equipmentImage)
        val equipmentAvailability = listItemView.findViewById<TextView>(R.id.equipmentAvailability)

        setAvailability(currentEquipment?.available, equipmentAvailability)

        equipmentNameView.text = currentEquipment!!.name
        Glide.with(context).load(currentEquipment.equipmentImage).into(equipmentImageView)

        listItemView.setOnClickListener {
            itemClick(currentEquipment.id)
        }

        return listItemView
    }

    private fun setAvailability(available : Boolean?, view : TextView) : Unit {
        if(available == true) {
            view.setText("Available")
            view.setBackgroundResource(R.drawable.available_background)
            return
        }

        view.setText("Not Available")
        view.setBackgroundResource(R.drawable.unavailable_background)
    }

    private fun itemClick(equipmentId: Long) : Unit {
        val equipmentDescriptionIntent : Intent = Intent(context, EquipmentDescriptionLayoutActivity::class.java)
        equipmentDescriptionIntent.putExtra("equipmentId", equipmentId)
        context.startActivity(equipmentDescriptionIntent)

    }



}
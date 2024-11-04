package com.bsit43.equicktrack.ui.home

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bsit43.equicktrack.R
import com.bsit43.equicktrack.auth.AuthViewModel
import com.bsit43.equicktrack.utils.currentDate
import com.bsit43.equicktrack.utils.selectedTimeIntoLocalDateTime
import com.google.android.material.button.MaterialButton
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

class HomeFragment : Fragment() {

    private lateinit var borrowButton : MaterialButton
    private lateinit var returnTimeButton : MaterialButton
    private lateinit var cancelButton : MaterialButton
    private lateinit var submitButton : MaterialButton
    private lateinit var historyListView : ListView
    private lateinit var emptyHistoryTextView : TextView
    private lateinit var purposeEditText : EditText
    private lateinit var equipmentImageView : ImageView
    private val equipmentBorrowDialog : Dialog by lazy {
        Dialog(requireActivity())
    }
    private val homeViewModel : HomeViewModel by activityViewModels()
    private val authViewModel : AuthViewModel by activityViewModels()

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if(result.contents != null) {
            val scannedData = result.contents
            val authToken = authViewModel.auth.value?.authToken
            val authUser = authViewModel.auth.value?.user
            if (authUser != null && !authToken.isNullOrEmpty()) {
                homeViewModel.getEquipmentByQrcode(authToken = authToken, qrcodeData = scannedData)
                return@registerForActivityResult
            }

            Toast.makeText(requireActivity(), "Unable to determine user", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireActivity(), "No Data Found", Toast.LENGTH_SHORT).show()
        }

    }

    private var _selectedHour : Int = -1
    private var _selectedMinute : Int = -1
    private var _amPm : String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)


        borrowButton.setOnClickListener {
            onClickBorrow()
        }

        returnTimeButton.setOnClickListener {
            val currentTime = currentDate()
            val minute = currentTime.minute
            val hour = currentTime.hour

            val timePickerDialog = TimePickerDialog(
                requireActivity(),
                { _, selectedHour, selectedMinute ->
                    _selectedHour = if (selectedHour == 0) 12 else selectedHour % 12
                    _selectedMinute = selectedMinute
                    _amPm = if (selectedHour >= 12) "PM" else "AM"
                    // Display the selected time in 12-hour format with AM/PM
                    returnTimeButton.text = String.format(
                        "%02d:%02d %s",
                        _selectedHour, _selectedMinute, _amPm
                    )
                },
                hour,
                minute,
                false
            )
            timePickerDialog.show()
        }

        val authToken = authViewModel.auth.value?.authToken
        val user = authViewModel.auth.value?.user

        Toast.makeText(requireActivity(), "User: ${user?.fullName}", Toast.LENGTH_SHORT).show()
        if (user != null && !authToken.isNullOrEmpty()) {
            homeViewModel.getUserTransaction(userId = user.id, authToken = authToken)
        }

        homeViewModel.selectedEquipment.observe(viewLifecycleOwner) {
            if(it == null) {
                Toast.makeText(requireActivity(), "Unable to determine equipment", Toast.LENGTH_SHORT).show()
                return@observe
            }

            equipmentBorrowDialog.show()
        }

        homeViewModel.userTransactions.observe(viewLifecycleOwner) {
            if(it.isNullOrEmpty()) {
                emptyTransactions()
            } else {
                showTransactions()
            }
        }

        homeViewModel.selectedEquipment.observe(viewLifecycleOwner) {
            if(it == null) {
                Toast.makeText(requireActivity(), "Unable to determine equipment", Toast.LENGTH_SHORT).show()
                return@observe
            }

            Toast.makeText(requireActivity(), "Equipment Name ${it.name}", Toast.LENGTH_SHORT).show()
        }

        cancelButton.setOnClickListener {
            purposeEditText.text.clear()
            equipmentBorrowDialog.dismiss()
        }

        submitButton.setOnClickListener {
            val borrowDate = currentDate()
            val selectedTime = selectedTimeIntoLocalDateTime(selectedHour = _selectedHour, selectedMinute = _selectedMinute, amPm = _amPm)
            Toast.makeText(requireActivity(), "Selected Time: $selectedTime", Toast.LENGTH_SHORT).show()
        }
    }


    private fun initViews(view : View) : Unit {
        borrowButton = view.findViewById(R.id.borrowButton)
        historyListView = view.findViewById(R.id.historyListView)
        emptyHistoryTextView = view.findViewById(R.id.emptyHistoryTextView)
        equipmentBorrowDialog.setCancelable(true)
        equipmentBorrowDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        equipmentBorrowDialog.setContentView(R.layout.borrow_equipment_dialog)
        equipmentBorrowDialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        purposeEditText = equipmentBorrowDialog.findViewById(R.id.purposeEditText)
        returnTimeButton = equipmentBorrowDialog.findViewById(R.id.returnTimeButton)
        submitButton = equipmentBorrowDialog.findViewById(R.id.submitButton)
        cancelButton = equipmentBorrowDialog.findViewById(R.id.cancelButton)
        equipmentImageView = equipmentBorrowDialog.findViewById(R.id.equipmentImage)
    }

    private fun onClickBorrow() : Unit {
        val options = ScanOptions()
        options.setPrompt("Scan Equipment")
        options.setOrientationLocked(true)
        options.setCaptureActivity(CaptureActivity::class.java)
        barcodeLauncher.launch(options)
    }

    private fun emptyTransactions() : Unit {
        emptyHistoryTextView.visibility = View.VISIBLE
        historyListView.visibility = View.GONE
    }

    private fun showTransactions() : Unit {
        emptyHistoryTextView.visibility = View.GONE
        historyListView.visibility = View.VISIBLE
    }

}
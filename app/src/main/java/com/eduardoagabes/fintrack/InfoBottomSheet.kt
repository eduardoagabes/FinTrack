package com.eduardoagabes.fintrack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InfoBottomSheet(
    private val title: String,
    private val description: String,
    private val btnText: String,
    private val onClicked: () -> Unit
) : BottomSheetDialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =
            inflater.inflate(R.layout.info_bottom_sheet, container, false)

        val tvTitle = view.findViewById<TextView>(R.id.tv_info_title)
        val tvDescr = view.findViewById<TextView>(R.id.tv_info_description)
        val btnActionInfo = view.findViewById<Button>(R.id.btn_info)

        tvTitle.text = title
        tvDescr.text = description
        btnActionInfo.text = btnText

        btnActionInfo.setOnClickListener {
            onClicked.invoke()
            dismiss()
        }

        return view
    }
}




package com.daryl.journalapp.core.utils

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import com.daryl.journalapp.R
import com.daryl.journalapp.databinding.LayoutConfirmationDialogBinding
import com.daryl.journalapp.databinding.LayoutDatePickerDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object DialogUtil {
    fun createConfirmationDialog(
        context: Context,
        header: String,
        body: String,
        onConfirm: () -> Unit,
        onCancel: () -> Unit = {}
    ) {
        val view = LayoutConfirmationDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = dialogBuilder(context, view).create()
        view.run {
            tvHeader.text = header
            tvBody.text = body
            mbYes.setOnClickListener {
                onConfirm()
                dialog.dismiss()
            }
            mbNo.setOnClickListener {
                onCancel()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun createDatePickerDialog(
        context: Context,
        resourceProvider: ResourceProvider,
        onConfirm: (String, String) -> Unit
    ) {
        val view = LayoutDatePickerDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = dialogBuilder(context, view).create()
        view.run {
            var startDate: String? = null
            var endDate: String? = null
            tvDate.text = resourceProvider.getString(R.string.start_date_header)
            mbSelectDate.setOnClickListener {
                val selectedDate = "${dpDate.dayOfMonth}/${dpDate.month + 1}/${dpDate.year}"
                when {
                    startDate == null -> {
                        tvDate.text = resourceProvider.getString(R.string.end_date_header)
                        startDate = selectedDate
                    }
                    startDate != null && endDate == null -> {
                        tvDate.text = resourceProvider.getString(R.string.final_date_header)
                        endDate = selectedDate
                    }
                }
            }
            mbYes.setOnClickListener {
                if(startDate != null && endDate != null) onConfirm(startDate!!, endDate!!)
                dialog.dismiss()
            }
            mbNo.setOnClickListener { dialog.dismiss() }
        }
        dialog.show()
    }

    private fun dialogBuilder(context: Context, view: ViewDataBinding): AlertDialog.Builder =
        MaterialAlertDialogBuilder(context).setView(view.root)
}
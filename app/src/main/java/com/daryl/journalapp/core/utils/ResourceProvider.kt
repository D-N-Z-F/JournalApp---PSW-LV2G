package com.daryl.journalapp.core.utils

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

class ResourceProvider(
    private val context: Context
) {
    fun getString(@StringRes resId: Int, vararg args: Any): String = context.getString(resId, *args)
    fun getColorList(@ColorRes resId: Int): ColorStateList = context.getColorStateList(resId)
}
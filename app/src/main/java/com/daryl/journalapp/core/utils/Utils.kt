package com.daryl.journalapp.core.utils

import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavOptions
import com.daryl.journalapp.core.Constants.DATE_TIME_FORMAT
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Utils {
    fun debugLog(): (Any) -> Unit = { Log.d("debugging", it.toString()) }
    fun String.capitalize() =
        this.substring(0, 1).uppercase() + this.substring(1).lowercase()
    fun popUpOptions(@IdRes destId: Int, bool: Boolean): NavOptions =
        NavOptions.Builder().setPopUpTo(destId, bool).setLaunchSingleTop(bool).build()
    fun getCurrentTime(): String =
        LocalDate.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
    fun parseTime(dateTime: String): LocalDate =
        LocalDate.parse(dateTime, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT))
}
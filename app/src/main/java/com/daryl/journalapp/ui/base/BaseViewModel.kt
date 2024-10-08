package com.daryl.journalapp.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

abstract class BaseViewModel: ViewModel() {
    protected val _signIn = MutableSharedFlow<String>()
    val signIn: SharedFlow<String> = _signIn
    protected val _submit = MutableSharedFlow<String>()
    val submit: SharedFlow<String> = _submit
    private val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    suspend fun <T> globalErrorHandler(func: suspend () -> T?): T? =
        try { func() }
        catch (e: Exception) {
            _error.emit(e.message.toString())
            e.printStackTrace()
            null
        }
}
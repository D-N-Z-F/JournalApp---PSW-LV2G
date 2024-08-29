package com.daryl.journalapp.ui.loginRegister

import androidx.lifecycle.viewModelScope
import com.daryl.journalapp.R
import com.daryl.journalapp.core.Constants.EMAIL_ERROR_MESSAGE
import com.daryl.journalapp.core.Constants.EMAIL_REGEX
import com.daryl.journalapp.core.Constants.LOGIN
import com.daryl.journalapp.core.Constants.NON_EXISTENT_USER
import com.daryl.journalapp.core.Constants.PASSWORD_ERROR_MESSAGE
import com.daryl.journalapp.core.Constants.PASSWORD_REGEX
import com.daryl.journalapp.core.Constants.REGISTER
import com.daryl.journalapp.core.Constants.REGISTRATION_FAILED
import com.daryl.journalapp.core.services.AuthService
import com.daryl.journalapp.core.utils.ResourceProvider
import com.daryl.journalapp.core.utils.Utils.capitalize
import com.daryl.journalapp.core.utils.Utils.debugLog
import com.daryl.journalapp.data.models.Validator
import com.daryl.journalapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(
    private val authService: AuthService,
    private val resourceProvider: ResourceProvider
): BaseViewModel() {
    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                authService.login(email, password)?.let {
                    _signIn.emit(
                        resourceProvider.getString(
                            R.string.success, LOGIN.capitalize()
                        )
                    )
                } ?: throw Exception(NON_EXISTENT_USER)
            }
        }
    }
    fun register(email: String, password: String, password2: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                val error = performValidation(email, password, password2)
                if(error != null) throw Exception(error)
                val isRegistered = authService.register(email, password)
                if(!isRegistered) throw Exception(REGISTRATION_FAILED)
                _signIn.emit(
                    resourceProvider.getString(
                        R.string.success, REGISTER.capitalize()
                    )
                )
            }
        }
    }
    private fun performValidation(email: String, password:String, password2: String) =
        Validator.validate(
            Validator(email, EMAIL_REGEX, EMAIL_ERROR_MESSAGE),
            Validator(password + password2, PASSWORD_REGEX, PASSWORD_ERROR_MESSAGE)
        )
}
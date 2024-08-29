package com.daryl.journalapp.ui.loginRegister

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.daryl.journalapp.R
import com.daryl.journalapp.core.Constants.BLACK
import com.daryl.journalapp.core.Constants.BLUE
import com.daryl.journalapp.core.Constants.LOGIN
import com.daryl.journalapp.core.Constants.REGISTER
import com.daryl.journalapp.core.Constants.WHITE
import com.daryl.journalapp.core.utils.ResourceProvider
import com.daryl.journalapp.databinding.FragmentLoginRegisterBinding
import com.daryl.journalapp.ui.base.BaseFragment
import com.daryl.journalapp.ui.base.BaseViewModel
import com.google.android.material.button.MaterialButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRegisterFragment : BaseFragment<FragmentLoginRegisterBinding>() {
    override val viewModel: LoginRegisterViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_login_register
    private var state: String = LOGIN
    private var colorList: Map<String, ColorStateList>? = null

    override fun onBindView(view: View) {
        super.onBindView(view)
        colorList = ResourceProvider(requireContext()).run {
            mapOf(
                BLUE to getColorList(R.color.blue),
                WHITE to getColorList(R.color.white),
                BLACK to getColorList(R.color.black)
            )
        }
        binding?.run {
            mbLogin.setOnClickListener {
                if(state == LOGIN) {
                    viewModel.login(etEmail.text.toString(), etPassword.text.toString())
                } else toggleState(it as MaterialButton)
            }
            mbRegister.setOnClickListener {
                if(state == REGISTER) {
                    viewModel.register(
                        etEmail.text.toString(),
                        etPassword.text.toString(),
                        etPassword2.text.toString()
                    )
                } else toggleState(it as MaterialButton)
            }
        }
    }
    private fun toggleState(clickedButton: MaterialButton) {
        state = if(state == LOGIN) REGISTER else LOGIN
        resetInputs()
        changeButtonAppearance(clickedButton)
    }
    private fun resetInputs() {
        binding?.run {
            setOf(etEmail, etPassword, etPassword2).forEach {
                it.setText("")
                if(it.id == etPassword2.id) it.visibility = invisible(state == LOGIN)
            }
        }
    }
    private fun changeButtonAppearance(clickedButton: MaterialButton) {
        binding?.run {
            val buttons = setOf(mbLogin, mbRegister)
            buttons.forEach { button ->
                val isClicked = button.id == clickedButton.id
                colorList?.let { list ->
                    val backgroundColor = if(isClicked) BLUE else WHITE
                    val textColor = if(isClicked) WHITE else BLACK
                    button.backgroundTintList = list[backgroundColor]
                    button.setTextColor(list[textColor])
                }
            }
        }
    }
}
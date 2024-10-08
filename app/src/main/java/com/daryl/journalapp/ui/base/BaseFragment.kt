package com.daryl.journalapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.daryl.journalapp.R
import com.daryl.journalapp.core.Constants.DELETE
import com.daryl.journalapp.core.Constants.ERROR
import com.daryl.journalapp.core.Constants.INFO
import com.daryl.journalapp.core.Constants.SUCCESS
import com.daryl.journalapp.ui.loginRegister.LoginRegisterFragmentDirections
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class BaseFragment<T: ViewDataBinding>: Fragment() {
    protected abstract val viewModel: BaseViewModel
    protected var binding: T? = null
    protected abstract fun getLayoutResource(): Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(getLayoutResource(), container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBindView(view)
        onBindData(view)
    }
    protected open fun onBindView(view: View) {
        binding = DataBindingUtil.bind(view)
    }
    protected open fun onBindData(view: View) {
        viewModel.run {
            lifecycleScope.launch {
                signIn.collect {
                    showSnackBar(view, it, INFO)
                    findNavController().navigate(
                        LoginRegisterFragmentDirections.actionLoginRegisterToHome()
                    )
                }
            }
            lifecycleScope.launch {
                submit.collect {
                    showToast(it)
                    if(!it.contains(DELETE)) findNavController().popBackStack()
                }
            }
            lifecycleScope.launch {
                error.collect { showSnackBar(view, it, ERROR) }
            }
        }
    }
    protected fun invisible(condition: Boolean): Int = if(condition) View.GONE else View.VISIBLE
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    private fun showSnackBar(view: View, message: String, type: String) {
        val backgroundTint = when(type) {
            INFO -> R.color.blue
            ERROR -> R.color.red
            SUCCESS -> R.color.green
            else -> R.color.grey
        }
        Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(requireContext(), backgroundTint))
            .show()
    }
}
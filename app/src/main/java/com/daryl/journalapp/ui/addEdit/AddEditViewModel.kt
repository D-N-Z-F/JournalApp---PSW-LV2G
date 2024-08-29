package com.daryl.journalapp.ui.addEdit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.daryl.journalapp.R
import com.daryl.journalapp.core.Constants.ADD
import com.daryl.journalapp.core.Constants.DESC_ERROR_MESSAGE
import com.daryl.journalapp.core.Constants.DESC_REGEX
import com.daryl.journalapp.core.Constants.EDIT
import com.daryl.journalapp.core.Constants.NON_EXISTENT_JOURNAL
import com.daryl.journalapp.core.Constants.SOMETHING_WENT_WRONG
import com.daryl.journalapp.core.Constants.TITLE_ERROR_MESSAGE
import com.daryl.journalapp.core.Constants.TITLE_REGEX
import com.daryl.journalapp.core.utils.ResourceProvider
import com.daryl.journalapp.core.utils.Utils.capitalize
import com.daryl.journalapp.core.utils.Utils.debugLog
import com.daryl.journalapp.data.models.Journal
import com.daryl.journalapp.data.models.Validator
import com.daryl.journalapp.data.repositories.JournalRepo
import com.daryl.journalapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repo: JournalRepo,
    private val resourceProvider: ResourceProvider
): BaseViewModel() {
    private val _journal = MutableStateFlow<Journal?>(null)
    val journal: StateFlow<Journal?> = _journal

    init {
        savedStateHandle.get<String>("id")?.let { if(it.isNotEmpty()) getJournalById(it) }
    }

    private fun getJournalById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                repo.getJournalById(id)?.let {
                    _journal.emit(it)
                } ?: throw Exception(NON_EXISTENT_JOURNAL)
            }
        }
    }
    fun createJournal(journal: Journal) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                val error = performValidation(journal.title, journal.description)
                if(error != null) throw Exception(error)
                repo.createJournal(journal)?.let {
                    _submit.emit(
                        resourceProvider.getString(
                            R.string.success, ADD.capitalize()
                        )
                    )
                } ?: throw Exception(SOMETHING_WENT_WRONG)
            }
        }
    }
    fun updateJournal(journal: Journal) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                val error = performValidation(journal.title, journal.description)
                if(error != null) throw Exception(error)
                val id = this@AddEditViewModel.journal.value?.id
                    ?: throw Exception(SOMETHING_WENT_WRONG)
                repo.updateJournal(
                    journal.copy(id = id)
                )
                _submit.emit(
                    resourceProvider.getString(
                        R.string.success, EDIT.capitalize()
                    )
                )
            }
        }
    }
    private fun performValidation(title: String, desc: String) =
        Validator.validate(
            Validator(title, TITLE_REGEX, TITLE_ERROR_MESSAGE),
            Validator(desc, DESC_REGEX, DESC_ERROR_MESSAGE)
        )
}
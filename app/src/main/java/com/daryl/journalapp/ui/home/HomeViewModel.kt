package com.daryl.journalapp.ui.home

import androidx.lifecycle.viewModelScope
import com.daryl.journalapp.R
import com.daryl.journalapp.core.Constants.DELETE
import com.daryl.journalapp.core.utils.ResourceProvider
import com.daryl.journalapp.data.models.Journal
import com.daryl.journalapp.data.repositories.JournalRepo
import com.daryl.journalapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: JournalRepo,
    private val resourceProvider: ResourceProvider
): BaseViewModel() {
    fun getAllJournals(): Flow<List<Journal>> = repo.getAllJournals()

    fun deleteJournal(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            globalErrorHandler {
                repo.deleteJournal(id)
                _submit.emit(
                    resourceProvider.getString(R.string.success, DELETE)
                )
            }
        }
    }
}
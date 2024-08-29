package com.daryl.journalapp.ui.addEdit

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.daryl.journalapp.R
import com.daryl.journalapp.core.Constants.ADD
import com.daryl.journalapp.core.Constants.EDIT
import com.daryl.journalapp.core.utils.Utils.getCurrentTime
import com.daryl.journalapp.data.models.Journal
import com.daryl.journalapp.databinding.FragmentAddEditBinding
import com.daryl.journalapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@AndroidEntryPoint
class AddEditFragment : BaseFragment<FragmentAddEditBinding>() {
    private var state: String = ADD
    override val viewModel: AddEditViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_add_edit
    override fun onBindView(view: View) {
        super.onBindView(view)
        initialSetup()
    }

    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.journal.collect { journal ->
                journal?.let {
                    toggleState()
                    setupExistingData(it)
                }
            }
        }
    }
    private fun toggleState() {
        state = if(state == ADD) EDIT else ADD
    }
    private fun setupExistingData(journal: Journal) {
        binding?.run {
            etTitle.setText(journal.title)
            etDesc.setText(journal.description)
            etLocation.setText(journal.location)
            val tags = journal.tags.joinToString()
            etTags.setText(tags)
        }
    }
    private fun initialSetup() {
        binding?.run {
            mbSubmit.setOnClickListener {
                val tags = etTags.text.toString().split(",").map { it.trim() }
                val journal = Journal(
                    title = etTitle.text.toString(),
                    description = etDesc.text.toString(),
                    dateTime = getCurrentTime(),
                    location = etLocation.text.toString(),
                    tags = tags
                )
                when(state) {
                    ADD -> viewModel.createJournal(journal)
                    EDIT -> viewModel.updateJournal(journal)
                    else -> {}
                }
            }
        }
    }
}
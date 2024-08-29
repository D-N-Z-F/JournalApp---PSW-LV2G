package com.daryl.journalapp.ui.home

import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daryl.journalapp.R
import com.daryl.journalapp.core.utils.DialogUtil
import com.daryl.journalapp.core.utils.DialogUtil.createConfirmationDialog
import com.daryl.journalapp.core.utils.DialogUtil.createDatePickerDialog
import com.daryl.journalapp.core.utils.ResourceProvider
import com.daryl.journalapp.core.utils.Utils.parseTime
import com.daryl.journalapp.data.models.Journal
import com.daryl.journalapp.databinding.FragmentHomeBinding
import com.daryl.journalapp.ui.adapters.JournalAdapter
import com.daryl.journalapp.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    @Inject
    lateinit var resourceProvider: ResourceProvider
    private lateinit var adapter: JournalAdapter
    override val viewModel: HomeViewModel by viewModels()
    override fun getLayoutResource(): Int = R.layout.fragment_home
    override fun onBindView(view: View) {
        super.onBindView(view)
        setupAdapter()
        initialSetup()
        setupSearch()
    }
    override fun onBindData(view: View) {
        super.onBindData(view)
        lifecycleScope.launch {
            viewModel.getAllJournals().collect {
                adapter.setJournals(it)
                triggerView(it)
            }
        }
    }
    private fun triggerView(journals: List<Journal>) {
        binding?.tvEmpty?.visibility = invisible(journals.isNotEmpty())
        binding?.rvJournals?.visibility = invisible(journals.isEmpty())
    }
    private fun setupSearch() {
        val queryListener = object: OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val filteredList = adapter.filterJournalsBySearch(it)
                    triggerView(filteredList)
                }
                return true
            }
        }
        binding?.svSearch?.setOnQueryTextListener(queryListener)
    }
    private fun initialSetup() {
        binding?.fabAdd?.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeToAddEdit("")
            )
        }
        binding?.mbDatePicker?.setOnClickListener {
            createDatePickerDialog(requireContext(), resourceProvider) { start, end ->
                val date1 = parseTime(start)
                val date2 = parseTime(end)
                val filteredList = adapter.filterJournalsByDateRange(date1, date2)
                triggerView(filteredList)
            }
        }
    }
    private fun setupAdapter() {
        adapter = JournalAdapter(emptyList(), resourceProvider)
        adapter.listener = object: JournalAdapter.Listener {
            override fun onClick(id: String) {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeToAddEdit(id)
                )
            }
            override fun onDelete(id: String) {
                createConfirmationDialog(
                    requireContext(),
                    resourceProvider.getString(R.string.confirm_header),
                    resourceProvider.getString(R.string.confirm_delete),
                    { viewModel.deleteJournal(id) }
                )
            }
        }
        binding?.rvJournals?.adapter = adapter
        binding?.rvJournals?.layoutManager = LinearLayoutManager(requireContext())
    }
}
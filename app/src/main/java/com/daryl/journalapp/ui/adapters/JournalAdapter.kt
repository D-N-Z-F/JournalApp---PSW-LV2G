package com.daryl.journalapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.daryl.journalapp.R
import com.daryl.journalapp.core.utils.ResourceProvider
import com.daryl.journalapp.core.utils.Utils.parseTime
import com.daryl.journalapp.data.models.Journal
import com.daryl.journalapp.databinding.ItemJournalBinding
import java.time.LocalDate
import java.time.LocalDateTime

class JournalAdapter(
    private var journals: List<Journal>,
    private val resourceProvider: ResourceProvider
): RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {
    private var originalList = emptyList<Journal>()
    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder =
        JournalViewHolder(
            ItemJournalBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(
        holder: JournalViewHolder, position: Int
    ) = holder.bind(journals[position])

    override fun getItemCount(): Int = journals.size

    fun setJournals(journals: List<Journal>) {
        this.journals = journals
        originalList = journals
        notifyDataSetChanged()
    }

    fun filterJournalsBySearch(query: String): List<Journal> {
        this.journals =
            if(query.isEmpty()) originalList
            else
                originalList.filter {
                    it.title.contains(query, ignoreCase = true) ||
                            it.location.contains(query, ignoreCase = true) ||
                            it.tags.any { tag -> tag.contains(query, ignoreCase = true)
                            }
            }
        notifyDataSetChanged()
        return this.journals
    }

    fun filterJournalsByDateRange(
        startDateTime: LocalDate, endDateTime: LocalDate
    ): List<Journal> {
        this.journals = this.journals.filter {
            val journalDate = parseTime(it.dateTime)
            journalDate in startDateTime .. endDateTime
        }
        notifyDataSetChanged()
        return this.journals
    }

    inner class JournalViewHolder(
        private val binding: ItemJournalBinding
    ): ViewHolder(binding.root) {
        fun bind(journal: Journal) {
            binding.run {
                val dateTimeLocation = "${journal.dateTime} ${journal.location}"
                tvDateTimeLocation.text = dateTimeLocation
                tvTitle.text = journal.title
                tvDesc.text = journal.description
                tvTags.text = resourceProvider.getString(R.string.tags, journal.tags.joinToString())
                llDetails.setOnClickListener { listener?.onClick(journal.id!!) }
                ivDelete.setOnClickListener { listener?.onDelete(journal.id!!) }
            }
        }
    }

    interface Listener {
        fun onClick(id: String)
        fun onDelete(id: String)
    }
}
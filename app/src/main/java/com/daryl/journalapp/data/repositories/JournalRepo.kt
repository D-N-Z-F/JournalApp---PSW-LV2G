package com.daryl.journalapp.data.repositories

import com.daryl.journalapp.core.Constants.JOURNAL_COLLECTION_PATH
import com.daryl.journalapp.core.Constants.NON_EXISTENT_USER
import com.daryl.journalapp.core.services.AuthService
import com.daryl.journalapp.core.utils.Utils.debugLog
import com.daryl.journalapp.data.models.Journal
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class JournalRepo(
    private val authService: AuthService
) {
    private fun getUid(): String = authService.getUid() ?: throw Exception(NON_EXISTENT_USER)

    private fun getCollection(): CollectionReference =
        Firebase.firestore.collection(JOURNAL_COLLECTION_PATH)

    fun getAllJournals() = callbackFlow<List<Journal>> {
        val listener = getCollection().addSnapshotListener { value, error ->
            if(error != null) throw error
            val journals = mutableListOf<Journal>()
            value?.documents?.map { snapshot ->
                snapshot.data?.let { map ->
                    val journal = Journal.fromMap(map)
                    journals.add(journal.copy(id = snapshot.id))
                }
            }
            trySend(journals)
        }
        awaitClose { listener.remove() }
    }

    suspend fun getJournalById(id: String): Journal? {
        val res = getCollection().document(id).get().await()
        return res.data?.let { Journal.fromMap(it).copy(id = res.id) }
    }

    suspend fun createJournal(journal: Journal): String? {
        val data = journal.copy(id = getUid())
        val res = getCollection().add(data.toMap()).await()
        return res?.id
    }

    suspend fun updateJournal(journal: Journal) {
        getCollection().document(journal.id!!).set(journal.toMap()).await()
    }

    suspend fun deleteJournal(id: String) {
        getCollection().document(id).delete().await()
    }
}
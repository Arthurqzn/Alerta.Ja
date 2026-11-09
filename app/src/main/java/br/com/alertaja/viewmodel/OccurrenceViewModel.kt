package br.com.alertaja.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import br.com.alertaja.data.*
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OccurrenceViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = Room.databaseBuilder(application, AppDatabase::class.java, "alertaja.db").build().occurrenceDao()
    val occurrences = dao.observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun save(title: String, category: String, neighborhood: String, description: String, severity: String, old: Occurrence? = null) {
        viewModelScope.launch {
            dao.save(Occurrence(id = old?.id ?: 0, title = title.trim(), category = category,
                neighborhood = neighborhood.trim(), description = description.trim(), severity = severity,
                status = old?.status ?: "Pendente", createdAt = old?.createdAt ?: System.currentTimeMillis()))
        }
    }
    fun toggleStatus(item: Occurrence) {
        viewModelScope.launch { dao.save(item.copy(status = if (item.status == "Pendente") "Resolvido" else "Pendente")) }
    }
    fun delete(item: Occurrence) { viewModelScope.launch { dao.delete(item.id) } }
}

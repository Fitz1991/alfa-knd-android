package ru.npc_ksb.alfaknd.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.npc_ksb.alfaknd.Repository.AlfaKndRepository

class AlfaKndViewModel(application: Application) : AndroidViewModel(application) {
    val TAG : String = "myLog"
    val repository: AlfaKndRepository
    val allDatum: LiveData<List<Datum>>

    init {
        //создаем БД
        //datumDao()- с какой таблицей работаеем
        val alfaKndDao = AppDatabase.getDatabase(application, viewModelScope).datumDao()

        repository = AlfaKndRepository(alfaKndDao)
        allDatum = repository.allDatum

    }

//    fun getById(id: Int) = viewModelScope.launch(Dispatchers.IO) {
//        repository.getById(id)
//    }
//
//    fun insert(datum: Datum) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insert(datum)
//        Log.d(TAG, "datum")
//    }
//
//    fun update(datum: Datum) = viewModelScope.launch(Dispatchers.IO) {
//        repository.update(datum)
//    }
}
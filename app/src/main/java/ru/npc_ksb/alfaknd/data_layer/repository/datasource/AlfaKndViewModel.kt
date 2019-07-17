package ru.npc_ksb.alfaknd.data_layer.repository.datasource

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import ru.npc_ksb.alfaknd.data_layer.repository.AlfaKndRepository

//класс для сохранения данных при повороте экрана
class AlfaKndViewModel(application: Application) : AndroidViewModel(application) {
    val TAG : String = "myLog"
    val repository: AlfaKndRepository
    val allTypesOfResponsibility: LiveData<List<TypesOfResponsibility>>
    val db : RoomDatabase
    init {
        //создаем БД
        //datumDao()- с какой таблицей работаеем
        db = AppDatabase.getDatabase(application, viewModelScope)
        val datumDao = db.datumDao()
        val actualDataDao = db.actualDataDao()
        val fieldDao = db.fieldDao()
        repository = AlfaKndRepository(datumDao, actualDataDao, fieldDao)
        allTypesOfResponsibility = repository.allTypesOfResponsibility
    }
}
package ru.npc_ksb.alfaknd.data_layer.repository.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.npc_ksb.alfaknd.presentation_layer.model.ActualData
import ru.npc_ksb.alfaknd.presentation_layer.model.Field
import ru.npc_ksb.alfaknd.data_layer.cache.dao.ActualDataDao
import ru.npc_ksb.alfaknd.data_layer.cache.dao.TypesOfResponsibilityDao
import ru.npc_ksb.alfaknd.data_layer.cache.dao.FieldDao

@Database(entities = arrayOf(
        TypesOfResponsibility::class,
        ActualData::class,
        Field::class
), version = 7)
public abstract class AppDatabase : RoomDatabase() {

    //метод, позволяющий производить операции над БД за счет интерфейса TypesOfResponsibilityDao
    abstract fun datumDao(): TypesOfResponsibilityDao
    abstract fun actualDataDao() : ActualDataDao
    abstract fun fieldDao() : FieldDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context,scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            //строим БД
            synchronized(this) {
                    val instance = Room.databaseBuilder(
                        //для работы с ресурсами
                        context.applicationContext,
                            //класс расширяющий  RoomDatabase
                    AppDatabase::class.java,
                    "Word_database"
                )
                            //при обновлении версии БД (изменении схемы), удаляются все данные
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        //Действия перед построением БД
        private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.datumDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(datumDao: TypesOfResponsibilityDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            //datumDao.deleteAll()
        }
    }


}
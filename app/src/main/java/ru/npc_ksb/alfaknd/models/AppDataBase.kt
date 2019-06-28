package ru.npc_ksb.alfaknd.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Datum::class), version = 3)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun datumDao(): DatumDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context,  scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Word_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope)).build()
                INSTANCE = instance
                return instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.datumDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(datumDao: DatumDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            //datumDao.deleteAll()
        }
    }


}
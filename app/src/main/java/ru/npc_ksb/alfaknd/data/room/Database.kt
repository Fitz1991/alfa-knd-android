package ru.npc_ksb.alfaknd.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.npc_ksb.alfaknd.data.room.dao.*
import ru.npc_ksb.alfaknd.data.room.entities.*


@Database(
    entities = arrayOf(
        Responsibility::class,
        Activity::class,
        ObjectAttribute::class,
        ActivityObject::class,
        SubjectAttribute::class,
        ActivitySubject::class,
        InspectionAnswerChecklist::class,
        InspectionChecklist::class,
        InspectionDocument::class,
        Inspections::class,
        InspectionImage::class,
        InspectionVideo::class
    ), version = 36
)
public abstract class AppDatabase : RoomDatabase() {

    abstract fun responsibilityDao(): ResponsibilityDao
    abstract fun activityDao(): ActivityDao
    abstract fun objectAttributeDao(): ObjectAttributeDao
    abstract fun activityObjectDao(): ActivityObjectDao
    abstract fun subjectAttributeDao(): SubjectAttributeDao
    abstract fun activitySubjectDao(): ActivitySubjectDao
    abstract fun inspectionAnswerChecklistDao(): InspectionAnswerChecklistDao
    abstract fun inspectionChecklistDao(): InspectionChecklistDao
    abstract fun inspectionDocumentDao(): InspectionDocumentDao
    abstract fun inspectionsDao(): InspectionsDao
    abstract fun inspectionsVideoDao(): InspectionVideoDao
    abstract fun inspectionsImageDao(): InspectionImageDao

    abstract fun syncInspectionDao(): SyncInspectionDao
    abstract fun syncAuthorityDao(): SyncAuthorityDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        //        https://stackoverflow.com/questions/805363/how-do-i-rename-a-column-in-a-sqlite-database-table
        val MIGRATION_10_11: Migration = object : Migration(10, 11) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Responsibility ADD COLUMN is_deleted INTEGER DEFAULT 0")
            }
        }


        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database"
                )
                    .addMigrations(MIGRATION_10_11)
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private class AppDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let {
                    scope.launch(Dispatchers.IO) {
                        //populateResponsibility(database.responsibilityDao())
                    }
                }
            }
        }

        fun populateResponsibility(responsibilityDao: ResponsibilityDao) {
            responsibilityDao.deleteAll()
        }
    }


}
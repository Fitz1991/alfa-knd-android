package ru.npc_ksb.alfaknd.data.room.dao

import android.util.Log
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import ru.npc_ksb.alfaknd.data.room.entities.*
import ru.npc_ksb.alfaknd.domain.sync.interfaces.BasicSyncCompleted
import timber.log.Timber

@Dao
abstract class SyncAuthorityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertResponsibilityList(responsibilityList: List<Responsibility>)
    @Delete
    abstract fun deleteResonsibilityList(deletedResonsibilityList: List<Responsibility>)

    @Transaction
    open fun sync(
        responsibilityList: List<Responsibility>,
        deletedResonsibilityList: List<Responsibility>,

    ) {
        Completable.concatArray(
            Completable.fromAction({ insertResponsibilityList(responsibilityList) }),
            Completable.fromAction({ deleteResonsibilityList(deletedResonsibilityList) }),
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .doOnComplete {

                Timber.d("------------------- Добавляемые Виды ответственности: ${responsibilityList.count()}")
                Timber.d("------------------- Удаляемые Виды ответственности: ${deletedResonsibilityList.count()}")
            }.subscribe()
    }
}
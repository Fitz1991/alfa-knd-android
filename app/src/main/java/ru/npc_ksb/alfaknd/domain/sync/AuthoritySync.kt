package ru.npc_ksb.alfaknd.domain.sync

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDateTime
import ru.npc_ksb.alfaknd.app.MyApplication
import ru.npc_ksb.alfaknd.data.models.MainModel
import ru.npc_ksb.alfaknd.data.preferences.AuthoritySyncData
import ru.npc_ksb.alfaknd.data.preferences.AuthoritySyncData.clearValues
import ru.npc_ksb.alfaknd.data.preferences.AuthoritySyncData.lastUpdateDateTime
import ru.npc_ksb.alfaknd.data.room.entities.*
import ru.npc_ksb.alfaknd.domain.requests.pojo.PaggingResponse
import ru.npc_ksb.alfaknd.domain.sync.interfaces.BasicSync
import ru.npc_ksb.alfaknd.utils.getUTCdatetimeAsString
import timber.log.Timber

class AuthoritySync(private var viewModel: MainModel) : BasicSync() {
    override val syncPreference : SharedPreferences = AuthoritySyncData.SyncPreference(MyApplication.context)
    override val lastUpdateDateTime: String = syncPreference.lastUpdateDateTime

    val resonsibilityList = mutableSetOf<Responsibility>()
    val deletedResonsibilityList = mutableSetOf<Responsibility>()

    @Suppress("UNCHECKED_CAST")
    override fun synchronize() : Flowable<PaggingResponse<out BaseEnity>>{
        return Flowable.concatArray(
            SyncResponsibility().remoteData(lastUpdateDateTime, pageSize)
        )
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                Timber.d("-------------- Старт синхронизации КНО: ${LocalDateTime.now()}")
            }
            .doOnComplete {
                syncPreference.lastUpdateDateTime = getUTCdatetimeAsString()
                viewModel.syncAuthorityRepository.sync(
                    resonsibilityList.toList(),
                    deletedResonsibilityList.toList(),


                )
            }
            .doOnNext{
                if (it.data.isNotEmpty()) {
                    val changedItems = it.data.filter { item -> !item.isDeleted }
                    val deletedItems = it.data.filter { item -> item.isDeleted }

                    val item = it.data.first()
                    if (item is Responsibility) {
                        resonsibilityList.addAll(changedItems as List<Responsibility>)
                        deletedResonsibilityList.addAll(deletedItems as List<Responsibility>)
                    }
                }
            }
    }

    override fun cleanData(): Completable {
        syncPreference.clearValues()
        return Completable.concatArray(
                Completable.fromCallable {viewModel.responsibilityRepository.deleteAll()}
        )
    }
}
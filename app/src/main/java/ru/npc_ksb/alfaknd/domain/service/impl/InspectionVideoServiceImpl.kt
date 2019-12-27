package ru.npc_ksb.alfaknd.domain.service.impl

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.npc_ksb.alfaknd.data.models.MainModel
import ru.npc_ksb.alfaknd.domain.service.InspectionVideoService
import timber.log.Timber
import java.util.*

class InspectionVideoServiceImpl(override var viewModel: MainModel) : InspectionVideoService {
    override fun getInspectionVideos(inspectionUUID: String): LiveData<List<InspectionVideo>> {
        return viewModel.inspectionsVideoDao.getInspectionVideos(inspectionUUID)
    }

    override fun saveInspectionVideo(inspectionVideo: InspectionVideo){
        Completable.fromAction({
            inspectionVideo.uuid = UUID.randomUUID().toString()
            viewModel.inspectionsVideoDao.insert(inspectionVideo)
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({},{
                    Timber.d( "save ${it}")
                })
    }

    override fun deleteInspectionVideo(inspectionVideo: InspectionVideo){
        Completable.fromAction({
            viewModel.inspectionsVideoDao.delete(inspectionVideo)
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({},{})
    }

}
package ru.npc_ksb.alfaknd.domain.service.impl

import androidx.lifecycle.LiveData
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.npc_ksb.alfaknd.data.models.MainModel
import ru.npc_ksb.alfaknd.domain.service.InspectionImageService
import timber.log.Timber
import java.util.UUID

class InspectionImageServiceImpl(override var viewModel: MainModel) : InspectionImageService {
    override fun getInspectionImages(inspectionUUID: String): LiveData<List<InspectionImage>> {
        return viewModel.inspectionsImageDao.getInspectionImages(inspectionUUID)
    }

    override fun saveInspectionImage(inspectionImage: InspectionImage){
        Completable.fromAction({
            inspectionImage.uuid = UUID.randomUUID().toString()
            viewModel.inspectionsImageDao.insert(inspectionImage)
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({},{
                    Timber.d( "save ${it}")
                })
    }

    override fun deleteInspectionImage(inspectionImage: InspectionImage){
        Completable.fromAction({
            viewModel.inspectionsImageDao.delete(inspectionImage)
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({},{})
    }
}
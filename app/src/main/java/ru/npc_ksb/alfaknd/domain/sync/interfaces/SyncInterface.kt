package ru.npc_ksb.alfaknd.domain.sync.interfaces

import io.reactivex.Flowable
import ru.npc_ksb.alfaknd.domain.requests.pojo.PaggingResponse


interface SyncInterface<T: BaseEnity> {

    fun getApi(page: Int, lastUpdateDateTime:String, pageSize: Int): Flowable<PaggingResponse<T>>

    fun remoteData(lastUpdateDateTime:String, pageSize: Int): Flowable<PaggingResponse<T>> {
        return padding(1, lastUpdateDateTime, pageSize)
    }

    fun padding(page: Int, lastUpdateDateTime:String, pageSize: Int): Flowable<PaggingResponse<T>>{
        return getApi(page, lastUpdateDateTime, pageSize)
                .concatMap {
                    if (page < it.paginator.num_pages!!) {
                        Flowable.just(it).concatWith(
                            padding(
                                it.paginator.page!!.plus(1),
                                lastUpdateDateTime,
                                pageSize
                            )
                        )
                    } else {
                        Flowable.just(it)
                    }
                }
    }
}
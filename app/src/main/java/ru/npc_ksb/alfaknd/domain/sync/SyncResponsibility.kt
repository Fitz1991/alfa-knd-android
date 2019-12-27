package ru.npc_ksb.alfaknd.domain.sync

import io.reactivex.Flowable
import ru.npc_ksb.alfaknd.data.room.entities.Responsibility
import ru.npc_ksb.alfaknd.domain.requests.NetworkService
import ru.npc_ksb.alfaknd.domain.requests.pojo.PaggingResponse
import ru.npc_ksb.alfaknd.domain.sync.interfaces.SyncInterface


class SyncResponsibility: SyncInterface<Responsibility> {

    override fun getApi(page: Int, lastUpdateDateTime:String, pageSize: Int): Flowable<PaggingResponse<Responsibility>> {
        return NetworkService.instance.getCatalogApi().responsibility(lastUpdateDateTime, page, pageSize)
    }

}
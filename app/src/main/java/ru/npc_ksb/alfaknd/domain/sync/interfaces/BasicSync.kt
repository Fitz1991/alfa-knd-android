package ru.npc_ksb.alfaknd.domain.sync.interfaces

import android.content.SharedPreferences
import io.reactivex.Completable
import io.reactivex.Flowable
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.MyApplication
import ru.npc_ksb.alfaknd.domain.requests.pojo.PaggingResponse

abstract class BasicSync {
    val pageSize = MyApplication.resource.getString(R.string.api_page_size).toInt()

    open val syncPreference : SharedPreferences?=null
    open val lastUpdateDateTime :String?=null

    abstract fun synchronize(): Flowable<PaggingResponse<out BaseEnity>>

    abstract fun cleanData(): Completable
}
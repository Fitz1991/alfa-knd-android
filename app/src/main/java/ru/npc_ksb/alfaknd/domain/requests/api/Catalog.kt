package ru.npc_ksb.alfaknd.domain.requests.api

import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.npc_ksb.alfaknd.domain.requests.pojo.PaggingResponse
import ru.npc_ksb.alfaknd.data.room.entities.Responsibility


interface CatalogApi {
    @GET("catalog/responsibility/")
    fun responsibility(@Query("after") after: String, @Query("page") page: Int, @Query("page_size") pageSize: Int): Flowable<PaggingResponse<Responsibility>>
}
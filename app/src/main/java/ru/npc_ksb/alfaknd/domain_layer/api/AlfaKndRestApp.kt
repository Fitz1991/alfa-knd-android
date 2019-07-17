package ru.npc_ksb.alfaknd.domain_layer.api

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class AlfaKndRestApp {
    internal var alfaKndApp: AlfaKndRestApp? = null
    private var alfaKndApi: AlfaKndApi? = null
    private var retrofit: Retrofit? = null

    fun getAlfaKndApi(baseUrl: String): AlfaKndApi {
        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
        alfaKndApi = retrofit!!.create(AlfaKndApi::class.java!!)
        return alfaKndApi!!
    }

    companion object {
        val instance = AlfaKndRestApp()
    }
}
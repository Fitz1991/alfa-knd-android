package ru.npc_ksb.alfaknd.domain.requests

import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.npc_ksb.alfaknd.R
import ru.npc_ksb.alfaknd.app.MyApplication
import ru.npc_ksb.alfaknd.data.preferences.BindData
import ru.npc_ksb.alfaknd.data.preferences.BindData.host
import ru.npc_ksb.alfaknd.data.preferences.BindData.token
import ru.npc_ksb.alfaknd.domain.requests.api.*


class NetworkService private constructor() {
    private val mRetrofit: Retrofit

    init {
        val bindPreference = BindData.BindPreference(MyApplication.context)
        val httpClient = OkHttpClient().newBuilder()
        val interceptor = Interceptor { chain ->
            val request = chain.request().newBuilder().addHeader("Authorization", "Token ${bindPreference.token}").build()
            chain.proceed(request)
        }
        httpClient.networkInterceptors().add(interceptor)
        val client = httpClient.build()

        val gson = GsonBuilder()
                .setDateFormat("dd.MM.yyyy'T'HH:mm")
                .create()

        val baseURL = bindPreference.host + MyApplication.context.resources.getString(R.string.api_url)
        mRetrofit = Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()
    }

    companion object {
        private var mInstance: NetworkService? = null

        val instance: NetworkService
            get() {
                if (mInstance == null) {
                    mInstance = NetworkService()
                }
                return mInstance!!
            }
    }

    fun getDeviceApi(): DeviceApi {
        return mRetrofit.create(DeviceApi::class.java)
    }

    fun getCatalogApi(): CatalogApi {
        return mRetrofit.create(CatalogApi::class.java)
    }

    fun getActivityApi(): ActivityApi {
        return mRetrofit.create(ActivityApi::class.java)
    }

    fun getActivityObjectApi(): ActivityObjectApi {
        return mRetrofit.create(ActivityObjectApi::class.java)
    }

    fun getActivitySubjectApi(): ActivitySubjectApi {
        return mRetrofit.create(ActivitySubjectApi::class.java)
    }

    fun getObjectAttributeApi(): ObjectAttributeApi {
        return mRetrofit.create(ObjectAttributeApi::class.java)
    }

    fun getSubjectAttributeApi(): SubjectAttributeApi {
        return mRetrofit.create(SubjectAttributeApi::class.java)
    }

    fun getInspectionsApi(): InspectionsApi {
        return mRetrofit.create(InspectionsApi::class.java)
    }

    fun getInspectionDocumentApi(): InspectionDocumentApi {
        return mRetrofit.create(InspectionDocumentApi::class.java)
    }

    fun getInspectionChecklistApi(): InspectionChecklistApi {
        return mRetrofit.create(InspectionChecklistApi::class.java)
    }

    fun getInspectionAnswerChecklistApi(): InspectionAnswerChecklistApi {
        return mRetrofit.create(InspectionAnswerChecklistApi::class.java)
    }




}
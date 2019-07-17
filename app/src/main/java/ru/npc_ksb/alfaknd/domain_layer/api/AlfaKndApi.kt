package ru.npc_ksb.alfaknd.domain_layer.api

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.*


interface AlfaKndApi {
//    @Headers("Content-Type:application/json")
//    @POST("identitytoolkit/v3/relyingparty/signupNewUser/")
//    abstract fun signupNewUser(@Query("key") key: String, @Body userInfo: UserInfo): Observable<UserInfo>
//
//    @Headers("Content-Type:application/json")
//    @POST("v1/projects/gotusit-c45c9/databases/(default)/documents/users/")
//    abstract fun addUserInfo(@Query("key") key: String, @Body userInfo: UserInfo): Observable<UserInfo>
//
//    @Headers("Content-Type:application/json")
//    @POST("/identitytoolkit/v3/relyingparty/signupNewUser/")
//    abstract fun testResp(@Query("key") key: String, @Body userInfo: UserInfo): Observable<UserInfo>
//
//    @GET("users/{user}/repos")
//    fun listRepos(@Path("user") user: String): Call<List<Repo>>
////    http://knd.ksb-dev/api/mobile/v1/catalog/responsibility/
}
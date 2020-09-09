package ru.uomkri.konturtest.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import ru.uomkri.konturtest.BuildConfig
import ru.uomkri.konturtest.utils.Config.BASE_URL

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val logging = HttpLoggingInterceptor()
val httpClient = OkHttpClient.Builder().addInterceptor(logging.apply {
    level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.NONE
}).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(httpClient)
    .build()

interface UserResourceService {
    @GET("json/generated-0{source}.json")
    fun getUsersFromSource(@Path(value = "source") source: Int): Deferred<List<NetUser>>
}

object UserResource {
    val retrofitService: UserResourceService by lazy {
        retrofit.create(UserResourceService::class.java)
    }
}
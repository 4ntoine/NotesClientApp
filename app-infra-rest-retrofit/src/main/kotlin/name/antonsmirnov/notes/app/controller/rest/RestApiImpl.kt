package name.antonsmirnov.notes.app.controller.rest

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun buildRestApiImpl(protocol: String, host: String, port: UInt) : RestApi {
    val gson = GsonBuilder().setLenient().create()
    val retrofit = Retrofit.Builder()
        .baseUrl("$protocol://$host:$port")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
    return retrofit.create(RestApi::class.java)
}
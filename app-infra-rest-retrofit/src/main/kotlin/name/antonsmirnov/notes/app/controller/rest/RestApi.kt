package name.antonsmirnov.notes.app.controller.rest

import name.antonsmirnov.notes.usecase.AddNote
import name.antonsmirnov.notes.usecase.ListNotes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {
    @GET("/api/list")
    fun listNotes(): Call<ListNotes.Response>

    @GET("/api/add")
    fun addNote(@Query("title") title: String, @Query("body") body:String?): Call<AddNote.Response>

    companion object {
        lateinit var instance: RestApi
    }
}
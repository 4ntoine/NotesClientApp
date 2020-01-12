package name.antonsmirnov.notes.app.controller.rest

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.url
import name.antonsmirnov.notes.usecase.AddResponseJson
import name.antonsmirnov.notes.usecase.ListResponseJson
import name.antonsmirnov.notes.usecase.NoteJson

class RestApi(val baseUrl: String) {
    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    private inline fun apiPath(relativePath: String) = "/api$relativePath"

    suspend fun listNotes(): ListResponseJson = client.get {
        url {
            url(baseUrl)
            encodedPath = apiPath("/list")
        }
    }

    suspend fun addNote(note: NoteJson): AddResponseJson = client.get {
        url {
            url(baseUrl)
            encodedPath = apiPath("/add")
            parameters["title"] = note.title
            note.body?.let { parameters["body"] = it }
        }
    }
}
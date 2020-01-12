package name.antonsmirnov.notes.app.controller.rest

import name.antonsmirnov.notes.usecase.ListNotes

class ListNotesController(
    val api: RestApi
) : ListNotes {

    override suspend fun execute(): ListNotes.Response  {
        val response = api.listNotes()
        return ListNotes.Response(response.notes.map {
            ListNotes.Note(it.id, it.title, it.body)
        })
    }
}
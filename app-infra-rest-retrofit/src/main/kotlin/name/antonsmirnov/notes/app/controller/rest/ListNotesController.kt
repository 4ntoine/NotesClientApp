package name.antonsmirnov.notes.app.controller.rest

import name.antonsmirnov.notes.usecase.ListNotes

class ListNotesController(
    val api: RestApi
) : ListNotes {

    override suspend fun execute(): ListNotes.Response {
        val result = api.listNotes().execute()
        return if (result.isSuccessful)
            result.body()!!
        else
            throw Exception("Call failed")
    }
}
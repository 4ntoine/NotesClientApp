package name.antonsmirnov.notes.app.controller.rest

import name.antonsmirnov.notes.usecase.AddNote
import name.antonsmirnov.notes.usecase.NoteJson

class AddNoteController(
    val api: RestApi
) : AddNote {

    override suspend fun execute(request: AddNote.Request): AddNote.Response {
        val response = api.addNote(NoteJson(null, request.title, request.body))
        return AddNote.Response(response.id)
    }
}
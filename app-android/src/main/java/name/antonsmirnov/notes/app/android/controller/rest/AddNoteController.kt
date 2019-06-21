package name.antonsmirnov.notes.app.android.controller.rest

import name.antonsmirnov.notes.usecase.AddNote

class AddNoteController(
    val api: RestApi
) : AddNote {

    override fun execute(request: AddNote.Request): AddNote.Response {
        val result = api.addNote(request.title, request.body).execute()
        return if (result.isSuccessful)
            result.body()!!
        else
            throw Exception("Call failed")
    }
}
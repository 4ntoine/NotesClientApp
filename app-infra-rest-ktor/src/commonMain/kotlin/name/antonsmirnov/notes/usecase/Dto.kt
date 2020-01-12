package name.antonsmirnov.notes.usecase

import kotlinx.serialization.Serializable

/**
 * Add response model
 */
@Serializable
data class AddResponseJson(val id: String)

@Serializable
data class NoteJson(
    val id: String?,
    val title: String,
    val body: String?)

/**
 * List response model
 */
@Serializable
data class ListResponseJson(val notes: Array<NoteJson>) // using of `List` requires custom de-/serializer on K/N
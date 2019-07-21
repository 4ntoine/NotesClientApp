package name.antonsmirnov.notes.presenter

// here we reuse domain Note entity (that comes from Server app) as canonical entity in Client app
// though we can have separate entity + mappers
typealias Note = name.antonsmirnov.notes.domain.Note
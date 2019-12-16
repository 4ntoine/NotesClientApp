import 'dart:math';

import 'package:app_flutter/add_note/add_note_use_case.dart';
import 'package:app_flutter/list_notes/list_notes_use_case.dart';
import 'package:app_flutter/list_notes/note.dart';

class TestAddNoteInteractor extends AddNoteUseCase {
  final Random _random = Random();

  @override
  Future<AddNoteResponse> addNote(AddNoteRequest request) {
    return Future.value(AddNoteResponse(_random.nextInt(0xffffffff).abs().toString()));
  }
}

class TestListNotesInteractor extends ListNotesUseCase {
  final List<Note> _notes;
  TestListNotesInteractor(this._notes);

  @override
  Future<ListNotesResponse> listNotes() {
    return Future.value(ListNotesResponse(_notes));
  }
}
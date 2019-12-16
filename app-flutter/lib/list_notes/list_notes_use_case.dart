import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:json_annotation/json_annotation.dart';

import 'note.dart';

part 'list_notes_use_case.g.dart';

// API
abstract class ListNotesUseCase {
  Future<ListNotesResponse> listNotes();
}

// (no request needed)

// response
class ListNotesResponse {
  List<Note> notes;
  ListNotesResponse(this.notes);
}

// wrapper that waits for few seconds and then forward call to wrapped useCase
class WaitingListNotesInteractorWrapper extends ListNotesUseCase {
  final Duration _duration;
  final ListNotesUseCase _useCase;
  WaitingListNotesInteractorWrapper(this._duration, this._useCase);

  @override
  Future<ListNotesResponse> listNotes() {
    return Future.delayed(_duration, () => _useCase.listNotes());
  }
}

// wrapper that sets timeout
class TimeoutListNotesInteractorWrapper extends ListNotesUseCase {
  final Duration _duration;
  final ListNotesUseCase _useCase;
  TimeoutListNotesInteractorWrapper(this._duration, this._useCase);

  @override
  Future<ListNotesResponse> listNotes() {
    return _useCase.listNotes().timeout(_duration);
  }
}

// call `flutter packages pub run build_runner build` to generate .g.dart files

@JsonSerializable(createToJson: false)
// request DTO (json)
class ListNoteResponseJson {
  final String id;
  final String title;
  final String body;
  ListNoteResponseJson(this.id, this.title, this.body);

  factory ListNoteResponseJson.fromJson(Map<String, dynamic> json)
    => _$ListNoteResponseJsonFromJson(json);
}

@JsonSerializable(createToJson: false)
// response DTO (json)
class ListNotesResponseJson {
  final List<ListNoteResponseJson> notes;
  ListNotesResponseJson(this.notes);

  factory ListNotesResponseJson.fromJson(Map<String, dynamic> json)
    => _$ListNotesResponseJsonFromJson(json);
}

// impl that interacts with server over http (json)
class ServerListNotesInteractor extends ListNotesUseCase {
  final Uri _uri;
  final http.Client _client;
  ServerListNotesInteractor(this._uri, [this._client]);

  @override
  Future<ListNotesResponse> listNotes() async {
    final client = _client ?? http.Client();
    final response = await client.get(_uri);
    if (response.statusCode == 200) {
      final notes = ListNotesResponseJson.fromJson(json.decode(response.body));
      return ListNotesResponse(notes.notes.map((it) => Note(it.id, it.title, it.body)).toList());
    } else {
      throw Exception("Unexpected HTTP status code: ${response.statusCode}");
    }
  }
}
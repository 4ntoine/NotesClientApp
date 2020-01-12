import 'dart:convert';

import 'package:app_flutter/http/factory.dart';
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

// call `flutter packages pub run build_runner build` to generate .g.dart files

@JsonSerializable(createToJson: false)
// request DTO (json)
class ListNoteResponseJson {
  final String id;
  final String title;
  final String body;
  ListNoteResponseJson(this.id, this.title, this.body);

  factory ListNoteResponseJson.fromJson(Map<String, dynamic> json) =>
      _$ListNoteResponseJsonFromJson(json);
}

@JsonSerializable(createToJson: false)
// response DTO (json)
class ListNotesResponseJson {
  final List<ListNoteResponseJson> notes;
  ListNotesResponseJson(this.notes);

  factory ListNotesResponseJson.fromJson(Map<String, dynamic> json) =>
      _$ListNotesResponseJsonFromJson(json);
}

// impl that interacts with server over http (json)
class ServerListNotesInteractor extends ListNotesUseCase {
  final Uri _uri;
  final HttpClientFactoryMethod _httpClientFactoryMethod;
  ServerListNotesInteractor(this._uri, [this._httpClientFactoryMethod]);

  @override
  Future<ListNotesResponse> listNotes() async {
    final httpClient = (_httpClientFactoryMethod ?? () => http.Client())();
    final response = await httpClient.get(_uri);
    try {
      if (response.statusCode == 200) {
        final notes = ListNotesResponseJson.fromJson(
            json.decode(response.body));
        return ListNotesResponse(
            notes.notes.map((it) => Note(it.id, it.title, it.body)).toList());
      } else {
        throw Exception('Unexpected HTTP status code: ${response.statusCode}');
      }
    } finally {
      httpClient.close();
    }
  }
}

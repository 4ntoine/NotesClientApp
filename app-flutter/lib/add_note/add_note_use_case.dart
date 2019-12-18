import 'dart:convert';

import 'package:http/http.dart' as http;
import 'package:json_annotation/json_annotation.dart';

part 'add_note_use_case.g.dart';

// API
abstract class AddNoteUseCase {
  Future<AddNoteResponse> addNote(AddNoteRequest request);
}

// response
class AddNoteResponse {
  final String id;
  AddNoteResponse(this.id);
}

// request
class AddNoteRequest {
  final String title;
  final String body;
  AddNoteRequest(this.title, this.body);
}

// call `flutter packages pub run build_runner build` to generate .g.dart files

@JsonSerializable(createFactory: false)
// request DTO (json)
class AddNoteRequestJson {
  final String id;
  AddNoteRequestJson(this.id);

  Map<String, dynamic> toJson()
    => _$AddNoteRequestJsonToJson(this);
}

@JsonSerializable(createToJson: false)
// response DTO (json)
class AddNoteResponseJson {
  final String id;
  AddNoteResponseJson(this.id);

  factory AddNoteResponseJson.fromJson(Map<String, dynamic> json)
    => _$AddNoteResponseJsonFromJson(json);
}

// impl that interacts with server over http (json)
class ServerAddNoteInteractor extends AddNoteUseCase {
  final Uri _uri;
  final http.Client _client;
  ServerAddNoteInteractor(this._uri, [this._client]);

  @override
  Future<AddNoteResponse> addNote(AddNoteRequest request) async {
    final client = _client ?? http.Client();
    final queryParams = {
      'title' : request.title,
      'body' : request.body
    };
    final uri = Uri(
      scheme: _uri.scheme,
      host: _uri.host,
      port: _uri.port,
      path: _uri.path,
      queryParameters: queryParams);
    final response = await client.get(uri);
    if (response.statusCode == 200) {
      final addResponse = AddNoteResponseJson.fromJson(json.decode(response.body));
      return AddNoteResponse(addResponse.id);
    } else {
      throw Exception('Unexpected HTTP status code: ${response.statusCode}');
    }
  }
}
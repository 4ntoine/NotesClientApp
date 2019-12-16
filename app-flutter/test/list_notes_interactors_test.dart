import 'package:app_flutter/list_notes/list_notes_use_case.dart';
import 'package:http/http.dart' as http;
import 'package:mockito/mockito.dart';
import 'package:test/test.dart';

import 'test_http_client.dart';

final url = Uri.parse('http://127.0.0.1:8080/api/list');

void main() {
  final client = MockHttpClient();
  const id1 = '1';
  const id2 = '2';
  const title1 = 'Title1';
  const title2 = 'Title2';
  const body1 = 'Body1';
  const body2 = 'Body2';

  test('ServerListNotesInteractor single note test', () async {
    when(client.get(url))
      .thenAnswer((_) async => http.Response(
        '{"notes": [ {"id":"$id1", "title":"$title1", "body":"$body1"} ]}',
        200));

    final interactor = ServerListNotesInteractor(url, client);
    final response = await interactor.listNotes();
    final notes = response.notes;

    expect(notes.length, equals(1));
    final note = notes.first;
    expect(note.id, equals(id1));
    expect(note.title, equals(title1));
    expect(note.body, equals(body1));
  });

  test('ServerListNotesInteractor multiple notes test', () async {
    when(client.get(url))
        .thenAnswer((_) async => http.Response(
        """
        {
          "notes": [
            { "id":"$id1", "title":"$title1", "body":"$body1" },
            { "id":"$id2", "title":"$title2", "body":"$body2" }
          ]
        }
        """, 200));

    final interactor = ServerListNotesInteractor(url, client);
    final response = await interactor.listNotes();
    final notes = response.notes;

    expect(notes.length, equals(2));
    final note1 = notes[0];
    expect(note1.id, equals(id1));
    expect(note1.title, equals(title1));
    expect(note1.body, equals(body1));

    final note2 = notes[1];
    expect(note2.id, equals(id2));
    expect(note2.title, equals(title2));
    expect(note2.body, equals(body2));
  });

  // TODO: add params escape test
  // TODO: add response validation test (eg. `string` expected, but `int` actual)
  // TODO: add server error test
}
import 'package:app_flutter/add_note/add_note_use_case.dart';
import 'package:http/http.dart' as http;
import 'package:mockito/mockito.dart';
import 'package:test/test.dart';

import 'test_http_client.dart';

final url = Uri.parse('http://127.0.0.1:8080/api/add');

void main() {
  const id = '12345';
  const title = 'Title1';
  const body = 'Body1';

  test('ServerAddNoteInteractor test', () async {
    final client = MockHttpClient();
    when(client.get(any))
        .thenAnswer((_) async => http.Response('{"id": "$id"}', 200));

    final interactor = ServerAddNoteInteractor(url, client);
    final response = await interactor.addNote(AddNoteRequest(title, body));

    expect(response.id, equals(id));

    final expectedUrl = "$url?title=$title&body=$body";
    expect(verify(client.get(captureAny)).captured.single.toString(), expectedUrl);
  });

  // TODO: add params escape test
  // TODO: add response validation test (eg. `string` expected, but `int` actual)
  // TODO: add server error test
}
import 'dart:convert';
import 'dart:typed_data';

import 'package:http/http.dart' as http;
import 'package:http/http.dart';

/// Adds delay to every request
class DelayWrapper implements http.Client {
  DelayWrapper(this._delay, this._client);

  final Duration _delay;
  final http.Client _client;

  @override
  Future<Response> get(url, {Map<String, String> headers}) {
    return Future.delayed(_delay,
            () => _client.get(url, headers: headers));
  }

  @override
  void close() {
    _client.close();
  }

  @override
  Future<StreamedResponse> send(BaseRequest request) {
    return Future.delayed(_delay,
            () => _client.send(request));
  }

  @override
  Future<Uint8List> readBytes(url, {Map<String, String> headers}) {
    return Future.delayed(_delay,
            () => _client.readBytes(url, headers: headers));
  }

  @override
  Future<String> read(url, {Map<String, String> headers}) {
    return Future.delayed(_delay,
            () => _client.read(url, headers: headers));
  }

  @override
  Future<Response> delete(url, {Map<String, String> headers}) {
    return Future.delayed(_delay,
            () => _client.delete(url, headers: headers));
  }

  @override
  Future<Response> patch(url, {Map<String, String> headers, body, Encoding encoding}) {
    return Future.delayed(_delay,
            () => _client.patch(url, headers: headers, body: body, encoding: encoding));
  }

  @override
  Future<Response> put(url, {Map<String, String> headers, body, Encoding encoding}) {
    return Future.delayed(_delay,
            () => _client.put(url, headers: headers, body: body, encoding: encoding));
  }

  @override
  Future<Response> post(url, {Map<String, String> headers, body, Encoding encoding}) {
    return Future.delayed(_delay,
            () => _client.post(url, headers: headers, body: body, encoding: encoding));
  }

  @override
  Future<Response> head(url, {Map<String, String> headers}) {
    return Future.delayed(_delay,
            () => _client.head(url, headers: headers));
  }
}
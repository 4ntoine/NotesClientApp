import 'dart:convert';
import 'dart:typed_data';

import 'package:http/http.dart' as http;
import 'package:http/http.dart';

/// Adds timeout to every request
class TimeoutWrapper implements http.Client {
  TimeoutWrapper(this._timeout, this._client);

  final Duration _timeout;
  final http.Client _client;

  @override
  Future<Response> get(url, {Map<String, String> headers}) {
    return _client
        .get(url, headers: headers)
        .timeout(_timeout);
  }

  @override
  void close() {
    _client.close();
  }

  @override
  Future<StreamedResponse> send(BaseRequest request) {
    return _client
        .send(request)
        .timeout(_timeout);
  }

  @override
  Future<Uint8List> readBytes(url, {Map<String, String> headers}) {
    return _client
        .readBytes(url, headers: headers)
        .timeout(_timeout);
  }

  @override
  Future<String> read(url, {Map<String, String> headers}) {
    return _client
        .read(url, headers: headers)
        .timeout(_timeout);
  }

  @override
  Future<Response> delete(url, {Map<String, String> headers}) {
    return _client
        .delete(url, headers: headers)
        .timeout(_timeout);
  }

  @override
  Future<Response> patch(url, {Map<String, String> headers, body, Encoding encoding}) {
    return _client
        .patch(url, headers: headers, body: body, encoding: encoding)
        .timeout(_timeout);
  }

  @override
  Future<Response> put(url, {Map<String, String> headers, body, Encoding encoding}) {
    return _client
        .put(url, headers: headers, body: body, encoding: encoding)
        .timeout(_timeout);
  }

  @override
  Future<Response> post(url, {Map<String, String> headers, body, Encoding encoding}) {
    return _client
        .post(url, headers: headers, body: body, encoding: encoding)
        .timeout(_timeout);
  }

  @override
  Future<Response> head(url, {Map<String, String> headers}) {
    return _client
        .head(url, headers: headers)
        .timeout(_timeout);
  }
}
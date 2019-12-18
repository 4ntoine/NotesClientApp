import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import 'add_note/add_note_screen.dart';
import 'add_note/add_note_use_case.dart';
import 'connection/connection_screen.dart';
import 'connection/connection_widget.dart';
import 'http/delay_wrapper.dart';
import 'http/timeout_wrapper.dart';
import 'list_notes/list_notes_screen.dart';
import 'list_notes/list_notes_use_case.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  static Uri _getMethodUri(BuildContext context, String method) {
    final connection = ConnectionWidget.of(context).holder.connection;
    return Uri(
        scheme: 'http',
        host: connection.host,
        port: connection.port,
        path: '/api/$method');
  }

  static Uri _getListNotesUri(BuildContext context) {
    return _getMethodUri(context, '/list');
  }

  static Uri _getAddNoteUri(BuildContext context) {
    return _getMethodUri(context, '/add');
  }

  static final _httpClient =
    TimeoutWrapper(
      Duration(seconds: 3),
      DelayWrapper( // help to better visualize it's long-running operation
        Duration(seconds: 1),
        http.Client()));

  static final connectionScreenBuilder = (BuildContext context) =>
      ConnectionScreen((Connection connection) {
        // URL/port input finished, remember url/port for all the other screens
        // and navigating to list screen
        ConnectionWidget.of(context).holder.connection = connection;
        ListNotesScreen.navigateTo(context);
      });

  static final addNoteScreenBuilder = (BuildContext context) => AddNoteScreen(
      ServerAddNoteInteractor(_getAddNoteUri(context), _httpClient),
      (_) {
        // "Add Note" successful, navigating to list screen
        ListNotesScreen.navigateTo(context);
      });

  static final listNotesScreenBuilder = (BuildContext context) => ListNotesScreen(
      ServerListNotesInteractor(_getListNotesUri(context), _httpClient),
      () {
        // "Add Note" tapped, navigating to according screen
        AddNoteScreen.navigateTo(context, false);
      });

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    var routes = {
      ConnectionScreen.route: connectionScreenBuilder,
      ListNotesScreen.route: listNotesScreenBuilder,
      AddNoteScreen.route: addNoteScreenBuilder};
    return ConnectionWidget(child: Platform.isAndroid
      ? MaterialApp(
          debugShowCheckedModeBanner: false,
          title: 'Notes',
          theme: ThemeData(primarySwatch: Colors.blue),
          initialRoute: ConnectionScreen.route,
          routes: routes)
      : CupertinoApp(
          debugShowCheckedModeBanner: false,
          title: 'Notes',
          initialRoute: ConnectionScreen.route,
          routes: routes
    ));
  }
}

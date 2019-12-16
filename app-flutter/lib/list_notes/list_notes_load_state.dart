import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:meta/meta.dart';

import 'note.dart';
import 'note_item_widget.dart';

// State:
@sealed
abstract class ListNotesLoadState {
  Widget getWidget();
}

// Loading
class ListNotesLoadingState extends ListNotesLoadState {
  @override
  Widget getWidget() {
    final indicator = Platform.isAndroid
        ? CircularProgressIndicator(value: null)
        : CupertinoActivityIndicator();
    return Center(child: indicator);
  }
}

// Loaded
class ListNotesLoadedState extends ListNotesLoadState {
  final List<Note> _notes;
  ListNotesLoadedState(this._notes);

  @override
  Widget getWidget() => ListView.builder(
    itemBuilder: (_, int index) => NoteItemWidget(this._notes[index]),
    itemCount: this._notes.length,
    padding: EdgeInsets.all(18.0));
}

// Error
class ListNotesLoadErrorState extends ListNotesLoadState {
  final Exception _error;
  ListNotesLoadErrorState(this._error);

  @override
  Widget getWidget() => Center(child: Text(
    _error.toString(),
    style: TextStyle(
      fontSize: 18.0,
      color: Colors.red,
      fontWeight: FontWeight.bold)
    ));
}
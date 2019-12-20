import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:meta/meta.dart';

// State:
@sealed
abstract class AddNoteLoadState {
  Widget getWidget();
}

// Loading
class AddNoteLoadingState extends AddNoteLoadState {
  @override
  Widget getWidget() {
    final indicator = Platform.isAndroid
        ? CircularProgressIndicator(value: null)
        : CupertinoActivityIndicator();
    return Center(child: indicator);
  }
}

// Loaded
class AddNoteLoadedState extends AddNoteLoadState {
  final String id;
  AddNoteLoadedState(this.id);

  @override
  Widget getWidget() => Center(child: Text('Note #$id added'));
}

// Error
class AddNoteLoadErrorState extends AddNoteLoadState {
  final Exception _error;
  AddNoteLoadErrorState(this._error);

  @override
  Widget getWidget() => Center(
      child: Text(_error.toString(),
          style: TextStyle(
              fontSize: 18.0, color: Colors.red, fontWeight: FontWeight.bold)));
}

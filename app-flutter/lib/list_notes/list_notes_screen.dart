import 'dart:io';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'list_notes_load_state.dart';
import 'list_notes_use_case.dart';

class ListNotesScreen extends StatefulWidget {
  static const route = '/listNotes';
  static navigateTo(BuildContext context, [bool cleanStack = true]) =>
      Navigator.pushNamedAndRemoveUntil(context, route, (_) => !cleanStack);

  final ListNotesUseCase _useCase;
  final VoidCallback _addNoteCallback;
  ListNotesScreen(this._useCase, this._addNoteCallback, {Key key}) : super(key: key);

  @override
  _ListNotesScreenState createState() => _ListNotesScreenState();
}

class _ListNotesScreenState extends State<ListNotesScreen> {
  ListNotesLoadState _state;

  Future<ListNotesResponse> _fetchNotes() async {
    return widget._useCase.listNotes();
  }

  @override
  initState() {
    super.initState();
    _loadNotes();
  }

  _loadNotes() {
    setState(() {
      _state = ListNotesLoadingState();
    });

    _fetchNotes().then((response) {
      setState(() {
        _state = ListNotesLoadedState(response.notes);
      });
    }).catchError((error) {
      setState(() {
        _state = ListNotesLoadErrorState(error);
      });
    });
  }

  @override
  Widget build(BuildContext context) {
    final addIcon = Platform.isAndroid ? Icons.add : CupertinoIcons.add;
    final refreshIcon = Platform.isAndroid ? Icons.refresh : CupertinoIcons.refresh;
    final double topMargin = Platform.isAndroid ? 0 : 85;

    return Platform.isAndroid
      ? Scaffold(
          appBar: AppBar(
            title: Text('Notes list'),
            actions: <Widget>[
              IconButton(icon: Icon(addIcon), onPressed: widget._addNoteCallback),
              IconButton(icon: Icon(refreshIcon), onPressed: () => _loadNotes())
            ]),
          body: _state.getWidget())
      : CupertinoPageScaffold(
          resizeToAvoidBottomInset: false,
          navigationBar: CupertinoNavigationBar(
            middle: Text('Notes list'),
            trailing: Row(
              mainAxisSize: MainAxisSize.min,
              children: <Widget>[
                CupertinoButton(
                  padding: EdgeInsets.zero, // important!
                  child: Text('Add'),
                  onPressed: widget._addNoteCallback),
                CupertinoButton(
                  padding: EdgeInsets.only(left: 8), // important!
                  child: Text('Refresh'),
                  onPressed: () => _loadNotes())
              ])),
          child: Padding(
            padding: EdgeInsets.only(top: topMargin),
            child: _state.getWidget()));
  }
}
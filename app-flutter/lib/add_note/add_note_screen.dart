import 'dart:io';

import 'package:app_flutter/add_note/add_note_load_state.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../main.dart';
import 'add_note_use_case.dart';

typedef AddNoteCallback = void Function(String id);

class _AddNoteScreenState extends State<AddNoteScreen> {
  final _titleController = TextEditingController();
  final _bodyController = TextEditingController();
  AddNoteLoadState _state;

  bool isValidInput() {
    // TODO: implement
    return true;
  }

  _onInputFinished() {
    if (!isValidInput()) {
      // TODO: show alert
      return;
    }

    setState(() {
      _state = AddNoteLoadingState();
      final request = AddNoteRequest(
        _titleController.text.trim(),
        _bodyController.text.trim());
      widget._useCase.addNote(request).then((response) {
        setState(() {
          final id = response.id;
          _state = AddNoteLoadedState(id);
          widget._callback(id);
        });
      }).catchError((error) {
        setState(() {
          _state = AddNoteLoadErrorState(error);
        });
      });
    });
  }

  Widget _getWidget() {
    final double interMargin = Platform.isAndroid ? 0 : 10;
    return _state != null
        ? _state.getWidget()
        : Column(children: <Widget> [
            Padding(
              padding: EdgeInsets.only(left: 10, right: 10),
              child: Platform.isAndroid // title
              ? TextField(
                  decoration: new InputDecoration(hintText: 'Title'),
                  maxLines: 1,
                  autofocus: true,
                  textInputAction: TextInputAction.next,
                  controller: _titleController)
              : CupertinoTextField(
                  maxLines: 1,
                  autofocus: true,
                  textInputAction: TextInputAction.next,
                  controller: _titleController
              )),
            Padding(
              padding: EdgeInsets.only(left: 10, top: interMargin, right: 10),
              child: Platform.isAndroid // body
                ? TextField(
                    decoration: new InputDecoration(hintText: 'Body'),
                    keyboardType: TextInputType.number,
                    maxLines: 1,
                    controller: _bodyController)
                : CupertinoTextField(
                    keyboardType: TextInputType.number,
                    maxLines: 1,
                    controller: _bodyController)),
            Platform.isAndroid
              ? RaisedButton(child: Text('OK'), onPressed: () { _onInputFinished(); })
              : CupertinoButton(child: Text('OK'), onPressed: () { _onInputFinished(); })
          ]);
  }

  @override
  Widget build(BuildContext context) {
    final double topMargin = Platform.isAndroid ? 0 : 105; // TODO: (why margin on iOS?)
    return Platform.isAndroid
      ? Scaffold(
          appBar: AppBar(title: Text('Add note')),
          body: _getWidget())
      : CupertinoPageScaffold(
          navigationBar: CupertinoNavigationBar(middle: Text('Add note')),
          child: Padding(
            padding: EdgeInsets.only(top: topMargin),
            child: _getWidget()));
  }
}

class AddNoteScreen extends StatefulWidget {
  static const route = '/addNote';
  static navigateTo(BuildContext context, [bool cleanStack = true]) =>
    Platform.isAndroid
      ? Navigator.pushNamedAndRemoveUntil(context, route, (_) => !cleanStack)
      : Navigator.push(context, CupertinoPageRoute(builder: MyApp.addNoteScreenBuilder));

  final AddNoteUseCase _useCase;
  final AddNoteCallback _callback;
  AddNoteScreen(this._useCase, this._callback, {Key key}) : super(key: key);

  @override
  _AddNoteScreenState createState() => _AddNoteScreenState();
}
import 'package:flutter/material.dart';

import 'note.dart';

class NoteItemWidget extends StatelessWidget {
  final Note _note;
  NoteItemWidget(this._note);

  @override
  Widget build(BuildContext context) => Padding(
    padding: EdgeInsets.only(bottom: 10),
    child: Align(
      alignment: Alignment.centerLeft,
      child: Column(children: <Widget>[
        Text(_note.title, style: TextStyle(
          fontWeight: FontWeight.bold,
          color: Colors.black,
          fontSize: 22.0)),
        Text(_note.body, style: TextStyle(
          color: Colors.black,
          fontSize: 18.0))
      ]))
  );
}
import 'dart:io';

import 'package:app_flutter/connection/connection_widget.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Connection {
  final String host;
  final int port;
  Connection(this.host, this.port);
}

typedef ConnectionScreenCallback = void Function(Connection connection);

class _ConnectionState extends State<ConnectionScreen> {
  final _hostController = TextEditingController(text: defaultConnection.host);
  final _portController =
      TextEditingController(text: defaultConnection.port.toString());

  _onInputFinished() {
    // TODO: add validation

    final connection = Connection(
        _hostController.text.trim(), int.tryParse(_portController.text.trim()));
    widget._callback(connection);
  }

  @override
  Widget build(BuildContext context) {
    final double topMargin =
        Platform.isAndroid ? 0 : 105; // TODO: (why margin on iOS?)
    final double interMargin = Platform.isAndroid ? 0 : 10;
    final body = Column(children: <Widget>[
      Padding(
          padding: EdgeInsets.only(left: 10, right: 10, top: topMargin),
          child: Platform.isAndroid // url
              ? TextField(
                  decoration: new InputDecoration(hintText: 'Host'),
                  maxLines: 1,
                  autofocus: true,
                  textInputAction: TextInputAction.next,
                  controller: _hostController)
              : CupertinoTextField(
                  maxLines: 1,
                  autofocus: true,
                  textInputAction: TextInputAction.next,
                  controller: _hostController)),
      Padding(
          padding: EdgeInsets.only(left: 10, top: interMargin, right: 10),
          child: Platform.isAndroid // port
              ? TextField(
                  decoration: new InputDecoration(hintText: 'Port'),
                  keyboardType: TextInputType.number,
                  maxLines: 1,
                  controller: _portController)
              : CupertinoTextField(
                  keyboardType: TextInputType.number,
                  maxLines: 1,
                  controller: _portController)),
      Platform.isAndroid
          ? RaisedButton(child: Text('OK'), onPressed: () => _onInputFinished())
          : CupertinoButton(
              child: Text('OK'), onPressed: () => _onInputFinished())
    ]);
    return Platform.isAndroid
        ? Scaffold(appBar: AppBar(title: Text('Server connection')), body: body)
        : CupertinoPageScaffold(
            navigationBar:
                CupertinoNavigationBar(middle: Text('Server connection')),
            child: body);
  }
}

class ConnectionScreen extends StatefulWidget {
  static const route = '/';

  final ConnectionScreenCallback _callback;
  ConnectionScreen(this._callback, {Key key}) : super(key: key);

  @override
  _ConnectionState createState() => _ConnectionState();
}

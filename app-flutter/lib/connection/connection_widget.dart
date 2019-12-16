import 'package:app_flutter/connection/connection_screen.dart';
import 'package:flutter/cupertino.dart';

final Connection defaultConnection = Connection('127.0.0.1', 8080);

class ConnectionHolder {
  Connection connection = defaultConnection;
  ConnectionHolder();
}

class ConnectionWidget extends InheritedWidget {
  final ConnectionHolder holder = ConnectionHolder();
  ConnectionWidget({Widget child}) : super(child: child);

  @override
  bool updateShouldNotify(ConnectionWidget oldWidget) {
    return holder.connection != oldWidget.holder.connection;
  }

  static ConnectionWidget of(BuildContext context) =>
    context.dependOnInheritedWidgetOfExactType<ConnectionWidget>();
}
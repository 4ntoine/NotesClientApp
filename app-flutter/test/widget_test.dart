import 'package:app_flutter/list_notes/list_notes_screen.dart';
import 'package:app_flutter/list_notes/note.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'test_use_case.dart';

void main() {
  const id1 = '1';
  const id2 = '2';
  const title1 = 'Title1';
  const title2 = 'Title2';
  const body1 = 'Body1';
  const body2 = 'Body2';

  testWidgets('Notes list is shown', (WidgetTester tester) async {
    final notes = [
      Note(id1, title1, body1),
      Note(id2, title2, body2),
    ];
    final interactor = TestListNotesInteractor(notes);
    // warning: `MaterialApp` (any App?) instance should be the root of the widget tree.
    // the test will just silently fail if it's `ListNotesScreen` instance the root
    final widget = MaterialApp(home: ListNotesScreen(interactor, null));
    await tester.pumpWidget(widget);

    // waiting for `Future` to be completed and state moved to `ListNotesLoadedState`.
    await tester.pumpAndSettle();

    expect(find.text('someInvalidString'), findsNothing);
    expect(find.text(title1), findsOneWidget);
    expect(find.text(title2), findsOneWidget);
    expect(find.text(body1), findsOneWidget);
    expect(find.text(body2), findsOneWidget);
  });
}

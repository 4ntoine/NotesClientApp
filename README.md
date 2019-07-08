[![Build Status](https://travis-ci.org/4ntoine/NotesClientApp.svg?branch=master)](https://travis-ci.org/4ntoine/NotesClientApp)

# The project

## Client-side

`app-mvp` is a client-side JVM MVP application skeleton for simple notes keeping in Kotlin.

`app-infra-rest-retrofit` is an infra layer - client REST controllers that are used by client apps.

### Mobile - Android

`app-android` is an Android app.

### Desktop - JavaFX

`app-javafx` is a desktop JavaFX app.

## Server-side

Client-side application requires [server-side JVM application](https://github.com/4ntoine/NotesServerApp) to be running.
Applications interact over HTTP REST. Make sure you run it before running Android application. 

# MVP

Both apps use MVP design pattern (see `app-mvp` module):

![MVP](images/arch/mvp.png?raw=true)

Android and JavaFX apps just provide concrete `View` implementations and reuse `Model` and `Presenter` from `app-mvp`.
In order to interact with server-side app `Model` has a reference to `UseCase` implementation (from server-side `app-api`).
In order to pass boundary we use according `Controller` that implement `UseCase` and abstracts transport/protocol.
So both apps use `app-infra-rest-retrofit` module with Controllers which can be easily replaced with another one.

![MVP](images/arch/sequence.png?raw=true)

# Building

Applications use `app-api` module from server-side application repository.

First, make sure:
* `NotesServerApp` artifacts are available on local Maven repository:
clone `NotesServerApp` repository to _any_ directory, build and publish it to local Maven repository
or
* use [Gradle composite-build](https://docs.gradle.org/current/userguide/composite_builds.html):
clone `NotesServerApp` repository to _../NotesServerApp_ directory (no separate building/publication is required).

## Android app

In this repository root directory:

	./gradlew app-android:assemble

Find compiled `.apk`s in `./app-android/build/outputs/apk/` directories.

## JavaFX app

In this repository root directory:

    ./gradlew app-javafx:shadowJar

Find compiled `app-javafx-all.jar` "shadow" jar (includes all dependencies) in `./app-javafx/build/libs/` directory.

# Testing

## Unit testing

One can find few unit tests that demonstrate some benefits of clean architecture for testing:

	./gradlew test

## Automated UI Testing

### Android app

First, create and run AVD. In this repository root directory:

	./gradlew app-android:connectedAndroidTest

Make sure all the tests passed:

    ...
    12:12:53 V/InstrumentationResultParser: Time: 15.482
    12:12:53 V/InstrumentationResultParser: 
    12:12:53 V/InstrumentationResultParser: OK (4 tests)
    12:12:53 V/InstrumentationResultParser: 
    12:12:53 V/InstrumentationResultParser: 
    12:12:53 V/InstrumentationResultParser: INSTRUMENTATION_CODE: -1
    12:12:53 V/InstrumentationResultParser: 
    12:12:53 I/XmlResultReporter: XML test result file generated at /Users/asmirnov/Documents/dev/src/Notes/NotesClientApp/app-android/build/outputs/androidTest-results/connected/TEST-Nexus_5X_API_25_-_dev(AVD) - 7.1.1-app-android-.xml. Total tests 4, passed 4, 

## Manual testing

### Android app

Run NotesServerApp, install Android app	to your device/emulator:

	adb install ./app-android/build/outputs/apk/debug/app-android-debug.apk

and run the app.

Type server-side app host and port:

![Server host and port](images/app/app-android/1_connection.png?raw=true)

Make sure you can see connection error:

![Connection error](images/app/app-android/2_1_listnotes_error.png?raw=true)

Run server-side app and click "Reload" button to reload the notes list in Android app:

![List notes loading progress](images/app/app-android/2_2_listnotes_progress.png?raw=true)
![Empty notes list](images/app/app-android/2_3_listnotes_empty.png?raw=true)

Click "Add" button and type note title and body:

![Add note](images/app/app-android/3_1_addnote_input.png?raw=true)
![Add note progress](images/app/app-android/2_2_listnotes_progress.png?raw=true)

Make sure you can see added note:

![Notes list](images/app/app-android/4_listnotes.png?raw=true)

### JavaFX app

Stop server-side app (just to test errors handling).
Run JavaFX app:

	java -jar app-javafx/build/libs/app-javafx-all.jar  

Type server-side app host and port:

![Server host and port](images/app/app-javafx/1_connection.png?raw=true)

Make sure you can see connection error:

![Connection error](images/app/app-javafx/2_1_listnotes_error.png?raw=true)

Run server-side app and click "Reload" button to reload the notes list in javafx app:

![List notes loading progress](images/app/app-javafx/2_2_listnotes_progress.png?raw=true)
![Empty notes list](images/app/app-javafx/2_3_listnotes_empty.png?raw=true)

Click "Add" button and type note title and body:

![Add note](images/app/app-javafx/3_1_addnote_input.png?raw=true)
![Add note progress](images/app/app-javafx/2_2_listnotes_progress.png?raw=true)

Make sure you can see added note:

![Notes list](images/app/app-javafx/4_listnotes.png?raw=true)

# Frameworks and tools

* [Kotlin](https://kotlinlang.org/) programming language
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) IDE for coding
* [Gradle](https://gradle.org/) with Groovy DSL for building
* [Retrofit2](https://square.github.io/retrofit/) for HTTP REST
* [JavaFX8](https://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html) for Java UI
* [Gradle Shadow plugin](https://github.com/johnrengelman/shadow) for building of jar file with all dependencies
* [Mockito](https://site.mockito.org/) and [Mockito-kotlin](https://github.com/nhaarman/mockito-kotlin) for unit-testing
* [Espresso](https://developer.android.com/training/testing/espresso) for Android UI testing

# Feedback

Please find TODOs in source code as topics for improvements if desired.

Any feedback and discussion is appreciated.
Contact me on e-mail for this or fork the repository and pull a request.

# Author

Anton Smirnov

dev [at] antonsmirnov [dot] name

2019
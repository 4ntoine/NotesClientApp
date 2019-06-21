[![Build Status](https://travis-ci.org/4ntoine/NotesClientApp.svg?branch=master)](https://travis-ci.org/4ntoine/NotesClientApp)

# The project

## Client-side

### Mobile - Android

This is a client-side JVM MVP application skeleton for simple notes keeping that interacts with server-side
JVM application via REST in Kotlin. Also it includes Android app based on that.

## Server-side

Client-side application requires [server-side JVM application](https://github.com/4ntoine/NotesServerApp) to be running.
Applications interact over HTTP REST. Make sure you run it before running Android application. 

# Building

Applications use `app-api` module from server-side application repository.

First, make sure:
* `NotesServerApp` artifacts are available on local Maven repository:
clone `NotesServerApp` repository to _any_ directory, build and publish it to local Maven repository
or
* use [Gradle composite-build](https://docs.gradle.org/current/userguide/composite_builds.html):
clone `NotesServerApp` repository to _../NotesServerApp_ directory (no separate building/publication is required).

Second, in this repository root directory:

	./gradlew assemble

# Testing

## Unit testing

One can find few unit tests that demonstrate some benefits of clean architecture for testing.

	./gradlew test
	
## Manual testing

Run NotesServerApp, install Android app	to your device and launch it.
Add a note, make sure you can see it in the list.

# Frameworks and tools

* [Kotlin](https://kotlinlang.org/) programming language
* [IntelliJ IDEA](https://www.jetbrains.com/idea/) IDE for coding
* [Gradle](https://gradle.org/) with Groovy DSL for building
* [Retrofit2](https://square.github.io/retrofit/) for HTTP REST
* [Mockito](https://site.mockito.org/) and [Mockito-kotlin](https://github.com/nhaarman/mockito-kotlin) for unit-testing

# Feedback

Please find TODOs in source code as topics for improvements if desired.

Any feedback and discussion is appreciated.
Contact me on e-mail for this or fork the repository and pull a request.

# Author

Anton Smirnov

dev [at] antonsmirnov [dot] name

2019
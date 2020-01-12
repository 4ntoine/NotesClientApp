//
//  DeserializerTests.swift
//  app-iosTests
//
//  Created by Anton Smirnov on 18/07/2019.
//  Copyright © 2019 Anton Smirnov. All rights reserved.
//

import XCTest
import app_ios_lib

class DeserializerTests: XCTestCase {

    let deserializer: SwiftDeserializer = SwiftJsonDeserializer()

    func testListNotesEmpty() throws {
        let json = #"{"notes":[]}"#
        let response = try deserializer.deserializeListNotes(data: Data(json.utf8))
        XCTAssertNotNil(response)
        XCTAssertNotNil(response.notes)
        let notes = response.notes as? [ListNotesNote]
        XCTAssertEqual(0, notes!.count)
    }
    
    func testListNotesOneNoteBothTitleAndBodyNotNil() throws {
        let json = #"{"notes":[{"id":"0e5fcb6f-54dd-4c6c-af24-b7c8b40bf71b","title":"hello","body":"world"}]}"#
        let response = try deserializer.deserializeListNotes(data: Data(json.utf8))
        XCTAssertNotNil(response)
        XCTAssertNotNil(response.notes)
        let notes = response.notes as? [ListNotesNote]
        XCTAssertEqual(1, notes!.count)
        XCTAssertEqual("0e5fcb6f-54dd-4c6c-af24-b7c8b40bf71b", notes!.first!.id)
        XCTAssertEqual("hello", notes!.first!.title)
        XCTAssertEqual("world", notes!.first!.body)
    }
    
    func testListNotesOneNoteBodyNil() throws {
        let json = #"{"notes":[{"id":"0e5fcb6f-54dd-4c6c-af24-b7c8b40bf71b","title":"hello"}]}"#
        let response = try deserializer.deserializeListNotes(data: Data(json.utf8))
        XCTAssertNotNil(response)
        XCTAssertNotNil(response.notes)
        let notes = response.notes as? [ListNotesNote]
        XCTAssertEqual(1, notes!.count)
        XCTAssertEqual("0e5fcb6f-54dd-4c6c-af24-b7c8b40bf71b", notes!.first!.id)
        XCTAssertEqual("hello", notes!.first!.title)
        XCTAssertNil(notes!.first!.body)
    }
    
    func testListNotesMultipleNotes() throws {
        let json = #"{"notes":[{"id":"0e5fcb6f-54dd-4c6c-af24-b7c8b40bf71b","title":"title1","body":"body1"},{"id":"07612eea-dd16-41ab-8ccc-f57f2ad5ae81","title":"title2","body":"body2"}]}"#
        let response = try deserializer.deserializeListNotes(data: Data(json.utf8))
        XCTAssertNotNil(response)
        XCTAssertNotNil(response.notes)
        let notes = response.notes as? [ListNotesNote]
        XCTAssertEqual(2, notes!.count)
        XCTAssertEqual("0e5fcb6f-54dd-4c6c-af24-b7c8b40bf71b", notes![0].id)
        XCTAssertEqual("title1", notes![0].title)
        XCTAssertEqual("body1", notes![0].body)
        XCTAssertEqual("07612eea-dd16-41ab-8ccc-f57f2ad5ae81", notes![1].id)
        XCTAssertEqual("title2", notes![1].title)
        XCTAssertEqual("body2", notes![1].body)
    }
    
    func testListNotesInvalid() {
        func assertInvalidJson(json: String) {
            XCTAssertThrowsError(try deserializer.deserializeListNotes(data: Data(json.utf8)), "") { error in
                XCTAssertNotNil(error as? RestApiError, "Failed to \(json): \(error)")
            }
        }
        
        assertInvalidJson(json: #"someInvalidJson"#) // invalid (no root object)
        assertInvalidJson(json: #"{}"#) // invalid (no "notes" property)
        assertInvalidJson(json: #"{"notes":[}"#) // invalid (missing "]")
        assertInvalidJson(json: #"{"notes":["title":"hello"}]}"#) // missing "id" property (required)
        assertInvalidJson(json: #"{"notes":[{"id":"0e5fcb6f-54dd-4c6c-af24-b7c8b40bf71b","body":"world"}]}"#) // missing "title" property
        assertInvalidJson(json: #"{"notes":[{"id":123,"title":"hello","body":"world"}]}"#) // wrong type of "id" field (string expected)
    }
    
    func testAddNote() throws {
        let json = #"{"id":"0e5fcb6f-54dd-4c6c-af24-b7c8b40bf71b"}"#
        let response = try deserializer.deserializeAddNote(data: Data(json.utf8))
        XCTAssertNotNil(response)
        XCTAssertNotNil(response.id)
        XCTAssertEqual("0e5fcb6f-54dd-4c6c-af24-b7c8b40bf71b", response.id)
    }
    
    func testAddNoteInvalid() {
        func assertInvalidJson(json: String) {
            XCTAssertThrowsError(try deserializer.deserializeAddNote(data: Data(json.utf8)), "Error") { error in
                XCTAssertNotNil(error as? RestApiError, "Failed to \(json): \(error)")
            }
        }
        
        assertInvalidJson(json: #"someInvalidJson"#) // invalid (no root object)
        assertInvalidJson(json: #"{}"#) // invalid (no "id" property)
        assertInvalidJson(json: #"{"id":""#) // invalid (missing """)
        assertInvalidJson(json: #"{"id":123}"#) // wrong type of "id" field (string expected)
    }
}

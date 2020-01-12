//
//  RestTests.swift
//  app-iosTests
//
//  Created by Anton Smirnov on 18/07/2019.
//  Copyright © 2019 Anton Smirnov. All rights reserved.
//

import XCTest
import app_ios_lib

class RestTests: XCTestCase {

    let impl = SwiftRestImpl(baseUrl: "http://127.0.0.1:8080", deserializer: SwiftJsonDeserializer())
    
    func testServerError() throws {
        let impl = RestImpl(baseUrl: "inexistingDomain.blabla", deserializer: JsonDeserializer())
        XCTAssertThrowsError(try impl.execute(), "Error") { error in
            XCTAssertNotNil(error as? RestApiError)
        }
    }

    // navigate to "http://localhost:8080/api/add?title=hello&body=world" once(!) before the test
    // use "http://localhost:8080/api/list" to make sure
    func testListNotes() throws {
        let response = try impl.execute()
        
        XCTAssertNotNil(response)
        XCTAssertNotNil(response.notes)
        let notesArray = response.notes as! [ListNotesNote]
        XCTAssertEqual(1, notesArray.count)
        let note = notesArray[0] as ListNotesNote
        XCTAssertEqual("hello", note.title)
        XCTAssertEqual("world", note.body)
    }
    
    // use "http://localhost:8080/api/list" to make sure it's added
    func testAddNote() throws {
        let request = AddNoteRequest.init(title: "hello", body: "world")
        let response = try impl.execute(request: request)
        
        XCTAssertNotNil(response)
        XCTAssertNotNil(response.id)
        print("Added note with id = \(response.id)")
        let uuid = UUID(uuidString: response.id)
        XCTAssertNotNil(uuid)
    }

}

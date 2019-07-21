//
//  RestTests.swift
//  app-iosTests
//
//  Created by Anton Smirnov on 18/07/2019.
//  Copyright © 2019 Anton Smirnov. All rights reserved.
//

import XCTest
@testable import app_ios
@testable import app_mvp

class RestTests: XCTestCase {

    let impl = RestImpl(baseUrl: "http://127.0.0.1:8080", deserializer: JsonDeserializer())
    
    func testServerError() throws {
        let impl = RestImpl(baseUrl: "inexistingDomain.blabla", deserializer: JsonDeserializer())
        XCTAssertThrowsError(try impl.execute(), "Error") { error in
            XCTAssertNotNil(error as? RestApiError)
        }
    }

    // navigate to "http://localhost:8080/add?title=hello&body=world" once(!) before the test
    // use "http://localhost:8080/list" to make sure
    func testListNotes() throws {
        let response = try impl.execute()
        
        XCTAssertNotNil(response)
        XCTAssertNotNil(response.notes)
        let notesArray = response.notes as! [DomainNote]
        XCTAssertEqual(1, notesArray.count)
        let note = notesArray[0] as DomainNote
        XCTAssertEqual("hello", note.title)
        XCTAssertEqual("world", note.body)
    }
    
    // use "http://localhost:8080/list" to make sure it's added
    func testAddNote() throws {
        let request = App_apiAddNoteRequest.init(title: "hello", body: "world")
        let response = try impl.execute(request: request)
        
        XCTAssertNotNil(response)
        XCTAssertNotNil(response.id)
        print("Added note with id = \(response.id)")
        let uuid = UUID(uuidString: response.id)
        XCTAssertNotNil(uuid)
    }

}

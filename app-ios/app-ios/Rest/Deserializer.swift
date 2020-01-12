//
//  Serialization.swift
//  app-ios
//
//  Created by Anton Smirnov on 18/07/2019.
//  Copyright © 2019 Anton Smirnov. All rights reserved.
//

import Foundation
import app_ios_lib

protocol SwiftDeserializer {
    func deserializeListNotes(data: Data) throws -> ListNotesResponse
    func deserializeAddNote(data: Data) throws -> AddNoteResponse
}

// alternative impl to K/N's one
// left just for an example
class SwiftJsonDeserializer : SwiftDeserializer {
    
    func deserializeListNotes(data: Data) throws -> ListNotesResponse {
        var notes = [ListNotesNote]()
        do {
            let jsonWithObjectRoot = try JSONSerialization.jsonObject(with: data,
                                                                      options: JSONSerialization.ReadingOptions())
            
            if let rootObject = jsonWithObjectRoot as? [String: Any] {
                if let notesArray = rootObject["notes"] as? [Any] {
                    for case let noteObject as [String : Any] in notesArray {
                        guard let id = noteObject["id"] as? String else { throw RestApiError.InvalidJson } // required field
                        guard let title = noteObject["title"] as? String else { throw RestApiError.InvalidJson } // required field
                        let body = noteObject["body"] as? String // optional field
                        let note = ListNotesNote(id: id, title: title, body: body)
                        notes.append(note)
                    }
                } else {
                    throw RestApiError.InvalidJson
                }
            } else {
                throw RestApiError.InvalidJson
            }
        } catch {
            throw RestApiError.InvalidJson
        }
        
        return ListNotesResponse.init(notes: notes)
    }
    
    func deserializeAddNote(data: Data) throws -> AddNoteResponse {
        do {
            let jsonWithObjectRoot = try JSONSerialization.jsonObject(with: data,
                                                                      options: JSONSerialization.ReadingOptions())
            if let rootObject = jsonWithObjectRoot as? [String: Any] {
                if let id = rootObject["id"] as? String {
                    return AddNoteResponse.init(id: id)
                } else {
                    throw RestApiError.InvalidJson
                }
            } else {
                throw RestApiError.InvalidJson
            }
        } catch {
            throw RestApiError.InvalidJson
        }
    }
}

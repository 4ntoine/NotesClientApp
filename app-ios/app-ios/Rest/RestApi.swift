//
//  RestApi.swift
//  app-ios
//
//  Created by Anton Smirnov on 15/07/2019.
//  Copyright © 2019 Anton Smirnov. All rights reserved.
//

import Foundation
import app_mvp

protocol RestApi : ListNotes, AddNote {
    
}

enum RestApiError: Error {
    case InvalidUrlError
    case ServerError(reason: Error)
    case InvalidJson
}

class RestImpl : RestApi {

    private let baseUrl: URL
    private let deserializer: Deserializer

    init(baseUrl: String, deserializer: Deserializer) {
        self.baseUrl = URL(string: baseUrl)!
        self.deserializer = deserializer
    }

    private func getUrl(relativeUrl: String) throws -> URL {
        guard let url = URL(string: relativeUrl, relativeTo: baseUrl) else {
            throw RestApiError.InvalidUrlError
        }
        return url
    }
    
    private func sendRequest(url: URL, requestBody: String = "") throws -> Data {
        var data: Data?
        var error: Error?
        let semaphore = DispatchSemaphore(value: 0)

        // adapt async nature of URLSession to sync nature of Notes API
        URLSession.shared.dataTask(with: url) { _data, _, _error in
            data = _data
            error = _error
            semaphore.signal()
        }.resume()

        _ = semaphore.wait(timeout: .distantFuture)
        if let reason = error {
            throw RestApiError.ServerError(reason: reason)
        }
        return data!
    }
    
    func execute() throws -> ListNotesResponse {
        let url = try getUrl(relativeUrl: "/api/list")
        let responseData = try sendRequest(url: url)
        return try deserializer.deserializeListNotes(data: responseData)
    }
    
    func execute(request: AddNoteRequest) throws -> AddNoteResponse {
        var urlWithParams = "/api/add?title=\(request.title)"
        if let body = request.body {
            urlWithParams += "&body=\(body)"
        }
        let url = try getUrl(relativeUrl: urlWithParams)
        let responseData = try sendRequest(url: url)
        return try deserializer.deserializeAddNote(data: responseData)
    }
}

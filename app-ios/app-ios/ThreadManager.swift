//
//  ThreadManager.swift
//  app-ios
//
//  Created by Anton Smirnov on 19/07/2019.
//  Copyright © 2019 Anton Smirnov. All rights reserved.
//

import Foundation
import app_mvp

// Executes in current thread
class BlockingThreadManager : ThreadManager {
    
    private var semaphore: DispatchSemaphore?
    
    func run(block: @escaping () -> Void) {
        block()
    }
    
    func waitForFinished() {
        // nothing
    }
}

// Executes in background thread
class BackgroundThreadManager : ThreadManager {
    
    var semaphore: DispatchSemaphore?
    
    func run(block: @escaping () -> Void) {
        semaphore = DispatchSemaphore(value: 0)
        DispatchQueue.global().async {
            block()
        }
    }
    
    func waitForFinished() {
        _ = semaphore!.wait(timeout: .distantFuture)
    }
}




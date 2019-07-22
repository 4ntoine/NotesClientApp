//
//  ViewController.swift
//  app-ios
//
//  Created by Anton Smirnov on 15/07/2019.
//  Copyright © 2019 Anton Smirnov. All rights reserved.
//

import UIKit
import app_mvp

class RestServerUrlModel : ServerUrlModel {
    override func update(host: String, port: UInt32) {
        super.update(host: host, port: port)
        initRestApi(host: host, port: port)
    }
    
    private func initRestApi(host: String, port: UInt32) {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        appDelegate.rest = RestImpl(baseUrl: "\(host):\(port)", deserializer: JsonDeserializer())
    }
}

class ServerUrlViewController: UIViewController, ServerUrlView {

    @IBOutlet weak var tfHost: UITextField!
    @IBOutlet weak var tfPort: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initButtons()
        initMvp()
    }
    
    private func initMvp() {
        let model = RestServerUrlModel.init(_host: "http://127.0.0.1",
                                        _port: KotlinUInt.init(integerLiteral: 8080))
        let presenter = ServerUrlPresenterImpl(model: model)
        presenter.attachView(view: self)
        self.presenter = presenter
    }
    
    private func initButtons() {
        navigationItem.title = "Connection"
        navigationItem.rightBarButtonItem = UIBarButtonItem(title: "OK",
                                                            style: .plain,
                                                            target: self,
                                                            action: #selector(okTapped(sender:)))
    }
    
    @objc func okTapped(sender: UIBarButtonItem) {
        (presenter as! ServerUrlPresenter).onViewChanged()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        (presenter as! ServerUrlPresenter).onViewDetached()
    }
    
    // as ServerUrlView
    
    var presenter: Any?
    
    func showValidationError(error: KotlinException) {
        let alert = UIAlertController(title: "Validation error",
                                      message: error.message,
                                      preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: "OK",
                                      style: UIAlertAction.Style.default,
                                      handler: nil))
        present(alert, animated: true, completion: nil)
    }
    
    func showNotesList() {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        appDelegate.navigateTo(viewControllerID: "ListNotesViewController")
    }
    
    var host: String {
        get {
            return tfHost.text!
        }
        set(newValue) {
            tfHost.text = newValue
        }
    }
    
    var port: String {
        get {
            return tfPort.text!
        }
        set(newValue) {
            tfPort.text = newValue
        }
    }

}

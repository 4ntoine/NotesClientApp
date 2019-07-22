//
//  AddNoteViewController.swift
//  app-ios
//
//  Created by Anton Smirnov on 20/07/2019.
//  Copyright © 2019 Anton Smirnov. All rights reserved.
//

import UIKit
import app_mvp

class AddNoteViewController: UIViewController, AddNoteView {
    
    @IBOutlet weak var tfTitle: UITextField!
    @IBOutlet weak var tfBody: UITextField!
    @IBOutlet weak var ivProgress: UIActivityIndicatorView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initButtons()
        initMvp()
    }
    
    private func initMvp() {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let useCase = appDelegate.rest!
        
        let note = DomainNote(title: "", body: nil)
        let model = AddNoteModel(useCase: useCase, note: note)
        // TODO: use BackgroundThreadManager (currently crashes for unknown reason)
        let presenter = AddNotePresenterImpl(model: model, threadManager: BlockingThreadManager())
        presenter.attachView(view: self)
        self.presenter = presenter
    }
    
    private func initButtons() {
        navigationItem.title = "Add note"
        navigationItem.rightBarButtonItem = UIBarButtonItem(title: "OK",
                                                            style: .plain,
                                                            target: self,
                                                            action: #selector(addTapped(sender:)))
    }
    
    @objc func addTapped(sender: Any) {
        (presenter as! AddNotePresenter).onViewChanged()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        (presenter as! AddNotePresenter).onViewDetached()
    }
    
    // as View
    
    var presenter: Any?
    
    private func showError(message: String) {
        let alert = UIAlertController(title: "Add note error",
                                      message: message,
                                      preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: "OK",
                                      style: UIAlertAction.Style.default,
                                      handler: nil))
        present(alert, animated: true, completion: nil)
    }
    
    func updateView(model_ model: AddNoteModel) {
        let model = model.stateCopy()
        
        DispatchQueue.main.async {
            switch model.state {
            case is AddNoteModel.StateExecuting:
                self.ivProgress.isHidden = false
                
            case is AddNoteModel.StateExecutionError:
                self.ivProgress.isHidden = true
                self.showError(message: (model.state as! AddNoteModel.StateExecutionError).error.message!)
                
            default:
                self.ivProgress.isHidden = true
                self.tfTitle.text = model.note.title
                self.tfBody.text = model.note.body
            }
        }
    }
    
    func updateModel(model: AddNoteModel) {
        model.note.title = self.tfTitle.text!
        model.note.body = self.tfBody.text
    }
    
    func showNotesList() {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        appDelegate.navigateTo(viewControllerID: "ListNotesViewController")
    }
    
}

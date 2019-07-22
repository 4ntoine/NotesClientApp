//
//  ListNotesViewController.swift
//  app-ios
//
//  Created by Anton Smirnov on 20/07/2019.
//  Copyright © 2019 Anton Smirnov. All rights reserved.
//

import UIKit
import app_mvp

// TODO: add loading indicator
class ListNotesViewController: UITableViewController, ListNotesView {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initButtons()
        initMvp()
        (presenter as! ListNotesPresenter).start()
    }
    
    private func initMvp() {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        let useCase = appDelegate.rest!
        
        let model = ListNotesModel(useCase: useCase)
        // TODO: use BackgroundThreadManager (currently crashes for unknown reason)
        let presenter = ListNotesPresenterImpl(model: model, threadManager: BlockingThreadManager())
        presenter.attachView(view: self)
        self.presenter = presenter
    }
    
    private func initButtons() {
        navigationItem.title = "Notes list"
        navigationItem.rightBarButtonItem = UIBarButtonItem(title: "Add",
                                                            style: .plain,
                                                            target: self,
                                                            action: #selector(addTapped(sender:)))
        
        navigationItem.leftBarButtonItem = UIBarButtonItem(title: "Reload",
                                                            style: .plain,
                                                            target: self,
                                                            action: #selector(loadTapped(sender:)))
    }
    
    @objc func addTapped(sender: Any) {
        let appDelegate = UIApplication.shared.delegate as! AppDelegate
        appDelegate.navigateTo(viewControllerID: "AddNoteViewController")
    }
    
    @objc func loadTapped(sender: Any) {
        (presenter as! ListNotesPresenter).onLoadRequest()
    }
    
    override func viewDidDisappear(_ animated: Bool) {
        super.viewDidDisappear(animated)
        (presenter as! ListNotesPresenter).onViewDetached()
    }
    
    // as UITableViewController
    
    override func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return notes.count
    }
    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: "NoteViewCell", for: indexPath) as? NoteViewCell  else {
            fatalError("The dequeued cell is not an instance of NoteViewCell.")
        }
        
        let note = notes[indexPath.row]
        cell.labTitle.text = note.title
        cell.labBody.text = note.body ?? ""
        
        return cell
    }
    
    // as View
    
    var presenter: Any?
    
    var notes = [DomainNote]()
    
    private func showError(message: String) {
        let alert = UIAlertController(title: "List notes error",
                                      message: message,
                                      preferredStyle: UIAlertController.Style.alert)
        alert.addAction(UIAlertAction(title: "OK",
                                      style: UIAlertAction.Style.default,
                                      handler: nil))
        present(alert, animated: true, completion: nil)
    }
    
    func updateView(model: ListNotesModel) {
        let _model = model.stateCopy()
        DispatchQueue.main.async {
            switch _model.state {
            case is ListNotesModel.StateLoaded:
                self.notes.removeAll()
                
                for note in (_model.state as! ListNotesModel.StateLoaded).notes as! [DomainNote] {
                    self.notes.append(note)
                }
                self.tableView.reloadData()
                
            case is ListNotesModel.StateLoadError:
                self.notes.removeAll()
                self.tableView.reloadData()
                self.showError(message: (_model.state as! ListNotesModel.StateLoadError).error.message!)
                
            default:
                self.notes.removeAll()
                self.tableView.reloadData()
            }
        }
    }
    
}

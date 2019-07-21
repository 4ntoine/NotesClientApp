//
//  NoteViewCell.swift
//  app-ios
//
//  Created by Anton Smirnov on 20/07/2019.
//  Copyright © 2019 Anton Smirnov. All rights reserved.
//

import UIKit

class NoteViewCell: UITableViewCell {

    @IBOutlet weak var labTitle: UILabel!
    @IBOutlet weak var labBody: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}

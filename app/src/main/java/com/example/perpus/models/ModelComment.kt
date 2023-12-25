package com.example.perpus.models

class ModelComment {

    var id = ""
    var bookId = ""
    var timestamp = ""
    var comment = ""
    var uid = ""

    constructor()

    constructor(id: String, bookId: String, timestamp: String, comment: String, iuid: String) {
        this.id = id
        this.bookId = bookId
        this.timestamp = timestamp
        this.comment = comment
        this.uid = iuid
    }


}
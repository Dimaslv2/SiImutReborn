package com.example.perpus

class ModelCategory {

    //variable
    var id:String = ""
    var category:String = ""
    var timestamp:Long = 0
    var uid:String = ""

    //empty constructor
    constructor()


    //parameter constructor
    constructor(id: String, category: String, timestamp: Long, uid: String) {
        this.id = id
        this.category = category
        this.timestamp = timestamp
        this.uid = uid
    }

}
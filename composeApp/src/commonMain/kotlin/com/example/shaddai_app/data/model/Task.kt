package com.example.shaddai_app.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Task : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var description: String = ""
    var isCompleted: Boolean = false
    var ownerId: String = ""
}

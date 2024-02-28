package com.mithrundeal.securebox.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class RealmObjectWrapper : RealmObject {
    @PrimaryKey
    var _id: ObjectId = BsonObjectId()
    var alias: String? = null
    var data: ByteArray? = null
    var iv: ByteArray? = null
}
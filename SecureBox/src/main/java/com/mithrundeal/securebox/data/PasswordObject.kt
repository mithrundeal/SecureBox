package com.mithrundeal.securebox.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class PasswordObject() : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var alias: String? = null

    /**If a unique id is not given, Securebox creates one.  */
    var uniqueAlias = RealmUUID.random()
    var data: ByteArray? = null
    var signedData: ByteArray? = null
    var iv: ByteArray? = null
}
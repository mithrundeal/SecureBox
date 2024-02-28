package com.mithrundeal.securebox.data

import android.accessibilityservice.GestureDescription.StrokeDescription
import com.mithrundeal.securebox.SecureBox
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class RealmDbHelper {
    companion object {
        private val realm: Realm by lazy {
            val config = RealmConfiguration.create(
                setOf(
                    PasswordObject::class, RealmObjectWrapper::class
                )
            )
            Realm.open(config)
        }

        fun getInstance() = realm
    }


}
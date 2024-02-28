package com.mithrundeal.securebox.keystore

import java.security.KeyStore
import java.security.KeyStore.Entry
import java.security.Signature

class SignatureHelper {

    companion object {
        fun sign(data: ByteArray): ByteArray? {
            val entry: Entry = EntryHelper.getAlias("signKey")

            if (entry is KeyStore.PrivateKeyEntry) {
                val signature: ByteArray = Signature.getInstance("SHA256withECDSA").run {
                    initSign(entry.privateKey)
                    update(data)
                    sign()
                }
                return signature
            }
            return null
        }

        fun verify(data: ByteArray, signature: ByteArray): Boolean {
            val entry: Entry = EntryHelper.getAlias("signKey")
            if (entry is KeyStore.PrivateKeyEntry) {
                val verify = Signature.getInstance("SHA256withECDSA").run {
                    initVerify(entry.certificate)
                    update(data)
                    verify(signature)
                }
                return verify
            }
            return false
        }
    }
}
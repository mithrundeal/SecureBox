package com.mithrundeal.securebox.keystore

import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec

class EncDecHelper {
    companion object {

        fun encrypt(data: ByteArray): EncryptResult {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val entry = EntryHelper.getAlias("encKey")

            if (entry is KeyStore.SecretKeyEntry) {
                cipher.init(Cipher.ENCRYPT_MODE, entry.secretKey)
            } else {
                //TODO error handle
            }
            return EncryptResult(cipher.doFinal(data), cipher.iv)
        }

        fun decrypt(data: ByteArray, iv: ByteArray): ByteArray? {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val entry = EntryHelper.getAlias("encKey")

            val gcmParameterSpec = GCMParameterSpec(128, iv)

            if (entry is KeyStore.SecretKeyEntry) {
                cipher.init(Cipher.DECRYPT_MODE, entry.secretKey, gcmParameterSpec)
            } else {
                //TODO error handle
            }
            return cipher.doFinal(data)
        }

    }
}
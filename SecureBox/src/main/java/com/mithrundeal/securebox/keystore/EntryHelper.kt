package com.mithrundeal.securebox.keystore

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.Enumeration
import javax.crypto.KeyGenerator

class EntryHelper {
    fun initSignatureKey() {
        if (!keyStore.containsAlias("signKey")) {
            val kpg: KeyPairGenerator =
                KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore")

            val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
                "signKey", KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
            ).run {
                setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                build()
            }

            kpg.initialize(parameterSpec)
            kpg.generateKeyPair()
        }
    }

    fun initEncryptionKey() {
        if (!keyStore.containsAlias("encKey")) {
            val kpg: KeyGenerator =
                KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")


            val parameterSpec: KeyGenParameterSpec = KeyGenParameterSpec.Builder(
                "encKey", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).run {
                setBlockModes(KeyProperties.BLOCK_MODE_GCM).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                build()
            }

            kpg.init(parameterSpec)
            kpg.generateKey()
        }
    }


    companion object {

        val keyStore: KeyStore by lazy {
            KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        }

        fun listAllAliases(): Enumeration<String> = keyStore.aliases()
        fun getAlias(alias: String): KeyStore.Entry = keyStore.getEntry(alias, null)
    }
}
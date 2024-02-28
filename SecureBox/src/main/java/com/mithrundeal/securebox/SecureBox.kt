package com.mithrundeal.securebox

import android.security.keystore.KeyInfo
import android.security.keystore.KeyProperties
import com.google.gson.Gson
import com.mithrundeal.securebox.data.PasswordObject
import com.mithrundeal.securebox.data.RealmDbHelper
import com.mithrundeal.securebox.data.RealmObjectWrapper
import com.mithrundeal.securebox.keystore.EncDecHelper
import com.mithrundeal.securebox.keystore.EntryHelper
import com.mithrundeal.securebox.keystore.SignatureHelper
import com.mithrundeal.securebox.utils.PasswordGeneratorBuilder
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import java.security.KeyFactory
import java.security.KeyStore

class SecureBox {

    companion object {
        private val pswg = PasswordGeneratorBuilder().build()
        private val realm: Realm by lazy {
            RealmDbHelper.getInstance()
        }
        private val secureBox: SecureBox by lazy {
            SecureBox()
        }

        fun getInstance() = secureBox
    }

    init {
        val helper = EntryHelper()
        helper.initEncryptionKey()
        helper.initSignatureKey()
    }

    /**
     * It's include
     * @includeUppercase,
     * includeLowerCase,
     * includeNumbers,
     * includeSymbols and 24 length random string but you can configure it.
     * @see PasswordGeneratorBuilder
     */
    fun getRandomPassword(): String {
        return pswg.generatePassword()
    }

    fun savePassword(alias: String, data: String) {
        val existingObject = (realm.query<PasswordObject>("alias = $0", alias)).find().count()
        if (existingObject == 0) {
            realm.writeBlocking {
                copyToRealm(PasswordObject().apply {
                    val result = EncDecHelper.encrypt(data.toByteArray())

                    this.data = result.data
                    this.iv = result.iv
                    this.alias = alias
                    this.signedData = SignatureHelper.sign(data.toByteArray())
                })
            }
        }
    }

    /**
     * Recommended to use this function to avoid StringPool.
     */
    fun savePassword(alias: String, data: ByteArray) {
        val existingObject = (realm.query<PasswordObject>("alias = $0", alias)).find().count()
        if (existingObject == 0) {
            realm.writeBlocking {
                copyToRealm(PasswordObject().apply {
                    val result = EncDecHelper.encrypt(data)

                    this.data = result.data
                    this.iv = result.iv
                    this.alias = alias
                    this.signedData = SignatureHelper.sign(data)
                })
            }
        }
    }

    fun getPassword(alias: String): ByteArray? {
        //var data = RealmDbHelper.getInstance().query<PasswordObject>("alias = $alias").find() wont work why?!
        val result = (realm.query<PasswordObject>("alias = $0", alias).find()).first()

        val decResult = EncDecHelper.decrypt(result.data!!, result.iv!!)

        if (SignatureHelper.verify(decResult!!, result.signedData!!)) {
            return decResult
        }
        return null
    }

    /**
     * Delete giving alias.
     */
    fun deletePassword(alias: String) {
        realm.writeBlocking {
            delete(query<PasswordObject>("alias = $0", alias))
        }
    }

    /**
     * It save object. if there is same alias saved before, you should delete it first.
     */
    fun <T : Any> saveData(alias: String, dataObject: T) {
        val existingObject = (realm.query<PasswordObject>("alias = $0", alias)).find().count()
        if (existingObject == 0) {
            val json = Gson().toJson(dataObject)
            val encResult = EncDecHelper.encrypt(json.toByteArray())
            realm.writeBlocking {
                copyToRealm(RealmObjectWrapper().apply {
                    this.data = encResult.data
                    this.iv = encResult.iv
                    this.alias = alias
                })
            }
        }
    }

    /**
     * @exception NullPointerException it's not null safe function. Consider before using this.
     */
    fun <T> getData(alias: String, clazz: Class<T>): T? {
        val data = (realm.query<RealmObjectWrapper>("alias = $0", alias)).find().first()
        val decResult = EncDecHelper.decrypt(data.data!!, data.iv!!)
        return Gson().fromJson(decResult!!.toString(Charsets.UTF_8), clazz)
    }

    fun deleteData(alias: String) {
        realm.writeBlocking {
            val query = this.query<RealmObjectWrapper>("alias = $0", alias).find()
            delete(query)
        }
    }

    /**
     *  The following class can be examined regarding the security level.
     *  @see KeyProperties
     *  @sample KeyProperties.SECURITY_LEVEL_STRONGBOX
     */
    fun isHardwareBackedTEE(): Int {
        val key = EntryHelper.getAlias("signKey") as KeyStore.PrivateKeyEntry
        val keyFactory = KeyFactory.getInstance(key.privateKey.algorithm, "AndroidKeyStore")
        val keyInfo = keyFactory.getKeySpec(key.privateKey, KeyInfo::class.java)
        return keyInfo.securityLevel
    }
}
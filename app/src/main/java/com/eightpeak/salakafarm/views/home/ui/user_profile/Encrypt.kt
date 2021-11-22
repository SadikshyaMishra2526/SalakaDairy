package com.eightpeak.salakafarm.views.home.ui.user_profile


import android.util.Base64
import com.google.android.gms.common.util.Hex
import com.google.gson.Gson
import org.apache.commons.codec.binary.Hex.encodeHex

import java.lang.Exception
import java.security.Key
import java.util.*
import javax.crypto.Cipher
import javax.crypto.Mac
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


object Encrypt {
    @Throws(Exception::class)
    fun encrypt(keyValue: ByteArray?, plaintext: String): String {
        val key: Key = SecretKeySpec(keyValue, "AES")
        //serialize
        val serializedPlaintext = "s:" + plaintext.toByteArray().size + ":\"" + plaintext + "\";"
        val plaintextBytes = serializedPlaintext.toByteArray(charset("UTF-8"))
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(Cipher.ENCRYPT_MODE, key)
        val iv = c.iv
        val encVal = c.doFinal(plaintextBytes)
        val encryptedData = Base64.encodeToString(encVal, Base64.NO_WRAP)
        val macKey = SecretKeySpec(keyValue, "HmacSHA256")
        val hmacSha256 = Mac.getInstance("HmacSHA256")
        hmacSha256.init(macKey)
        hmacSha256.update(Base64.encode(iv, Base64.NO_WRAP))
        val calcMac = hmacSha256.doFinal(encryptedData.toByteArray(charset("UTF-8")))
//        val mac: String = String(Hex.encodeHex(calcMac))
//        //Log.d("MAC",mac);
//        val aesData = AesEncryptionData(
//            Base64.encodeToString(iv, Base64.NO_WRAP),
//            encryptedData,
//            mac
//        )
//        val aesDataJson = Gson().toJson(aesData)
//        return Base64.encodeToString(aesDataJson.toByteArray(charset("UTF-8")), Base64.DEFAULT)
        return "hello"
    }


    class AesEncryptionData(var iv: String, var value: String, var mac: String)
}

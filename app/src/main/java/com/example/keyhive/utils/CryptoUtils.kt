package com.example.keyhive.utils

import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.util.Base64
import android.util.Log

class CryptoUtils {
    private val secretKey = Constants.PRIVATE_SECRET_KEY
    private val secretIV = Constants.PRIVATE_SECRET_IV
    fun encrypt(password:String): String{
        val iv = IvParameterSpec(this.secretIV.toByteArray())
        val keySpec = SecretKeySpec(this.secretKey.toByteArray(),"AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE,keySpec,iv)
        val encrypted = cipher.doFinal(password.toByteArray())
        Log.d("Encrypted",Base64.encodeToString(encrypted,Base64.DEFAULT))
        val encodedByte = Base64.encode(encrypted,Base64.DEFAULT)
        return String(encodedByte)
    }
    fun decrypt(password:String): String {
        val decodedByte = Base64.decode(password,Base64.DEFAULT)
        val iv = IvParameterSpec(this.secretIV.toByteArray())
        val keySpec = SecretKeySpec(this.secretKey.toByteArray(),"AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE,keySpec,iv)
        val decrypted = cipher.doFinal(decodedByte)
        return String(decrypted)
    }

}
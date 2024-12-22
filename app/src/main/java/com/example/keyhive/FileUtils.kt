package com.example.keyhive

import android.content.Context
import com.example.keyhive.model.Password
import java.io.File
import java.io.FileWriter

fun exportPasswordsToCSV(context: Context, passwords: List<Password>): File?{
    return try{
        val dir = context.getExternalFilesDir(null) ?: return null
        if (!dir.exists()) dir.mkdirs()
        val csvFile = File(dir, "passwords.csv")
        val writer = FileWriter(csvFile)
        writer.append("App/Service,Username,Password,Description\n")
        passwords.forEach { password ->
            writer.append("${password.type},${password.username},${password.password},${password.description}")
        }
        writer.flush()
        writer.close()
        csvFile
    }catch(e:Exception){
        e.printStackTrace()
        null

    }
}
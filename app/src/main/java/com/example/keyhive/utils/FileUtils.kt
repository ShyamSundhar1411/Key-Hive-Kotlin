package com.example.keyhive.utils

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.keyhive.model.Password
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter

fun exportPasswordsToCSV(context: Context, passwords: List<Password>): File?{
    return try{
        val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        if (!downloadsFolder.exists()) downloadsFolder.mkdirs()
        val filename = "passwords"+System.currentTimeMillis()+".csv"
        val csvFile = File(downloadsFolder, filename)
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
fun readCSVAndLogIt(context: Context,file: File){
    try{
        val fileInputStream = FileInputStream(file)
        val reader = fileInputStream.bufferedReader()
        var line: String? = reader.readLine()
        while(line != null){
            Log.d("CSV",line)
            line = reader.readLine()
        }
        reader.close()
        fileInputStream.close()
    }catch(e:Exception){
        e.printStackTrace()
        Log.e("CSV_FILE_ERROR", "Error reading CSV file", e)
    }
}
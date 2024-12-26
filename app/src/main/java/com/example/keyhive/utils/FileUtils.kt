package com.example.keyhive.utils

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.example.keyhive.model.Password
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter

fun exportPasswordsToCSV(context: Context, passwords: List<Password>): File?{
    return try{
        val downloadsFolder = File(context.filesDir.toString()+"/downloads")
        if(!downloadsFolder.exists()) downloadsFolder.mkdirs()
        val filename = "passwords"+System.currentTimeMillis()+".csv"
        val csvFile = File(downloadsFolder, filename)
        val writer = FileWriter(csvFile)
        writer.append("App/Service,Username,Password,Description\n")
        passwords.forEach { password ->
            writer.append("${password.type},${password.username},${CryptoUtils().decrypt(password.password)},${password.description}\n")
        }
        writer.flush()
        writer.close()
        csvFile
    }catch(e:Exception){
        e.printStackTrace()
        Log.e("CSV_EXPORT_ERROR", "Error exporting passwords to CSV", e)
        null

    }
}
fun shareCsvFile(context:Context,file:File){
    val uri = FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName+".fileprovider",
        file
    )
    val sendIntent = Intent(
        Intent.ACTION_SEND
    ).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM,uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val shareIntent = Intent.createChooser(sendIntent,null)
    startActivity(context,shareIntent,null)
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
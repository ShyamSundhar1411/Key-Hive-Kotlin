package com.axionlabs.keyhive.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.axionlabs.keyhive.model.ImportCSVResponse
import com.axionlabs.keyhive.model.Password
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.util.Date

fun exportPasswordsToCSV(context: Context, passwords: List<Password>): File? {
    return try {
        val downloadsFolder = File(context.filesDir.toString() + "/downloads")
        if (!downloadsFolder.exists()) downloadsFolder.mkdirs()
        val filename = "passwords" + System.currentTimeMillis() + ".csv"
        val csvFile = File(downloadsFolder, filename)
        val writer = FileWriter(csvFile)
        writer.append("App/Service,Username,Password,Description\n")
        passwords.forEach { password ->
            writer.append("${password.type},${password.username},${CryptoUtils().decrypt(password.password)},${password.description}\n")
        }
        writer.flush()
        writer.close()
        csvFile
    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("CSV_EXPORT_ERROR", "Error exporting passwords to CSV", e)
        null

    }
}

fun shareCsvFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName + ".fileprovider",
        file
    )
    val sendIntent = Intent(
        Intent.ACTION_SEND
    ).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(context, shareIntent, null)
}
fun getFileFromUri(uri: Uri, context: Context): File?{
    var file: File?=  null
    try{
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = uri.lastPathSegment ?: "imported_file.csv"
        file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

    }catch (e: Exception){
        e.printStackTrace()
    }
    return file
}
fun importPasswordsFromCSV(file: File): ImportCSVResponse {
    val importedPasswords = mutableListOf<Password>()
    var response: ImportCSVResponse

    try {
        val fileInputStream = FileInputStream(file)
        val reader = fileInputStream.bufferedReader()
        val headerLine = reader.readLine()
        val expectedHeader = "App/Service,Username,Password,Description"
        Log.d("CSV_IMPORT_HEADER", headerLine ?: "null")

        when {

            headerLine == null -> {
                response = ImportCSVResponse(
                    errorMessage = "Empty CSV File",
                    successMessage = "",
                    statusCode = 400,
                    passwords = importedPasswords,
                )
            }

            headerLine != expectedHeader -> {
                response = ImportCSVResponse(
                    errorMessage = "Invalid CSV format. Expected header: $expectedHeader",
                    successMessage = "",
                    statusCode = 400,
                    passwords = importedPasswords
                )
            }

            else -> {
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val values = line!!.split(",")
                    Log.d("CSV_IMPORT_VALUE", values.size.toString())
                    if (values.size >= 4) {
                        val password = Password(
                            type = values[0],
                            username = values[1],
                            password = CryptoUtils().encrypt(values[2]),
                            description = values[3],
                            createdAt = Date(),
                            updatedAt = Date()
                        )
                        importedPasswords.add(password)
                    } else {
                        Log.e("CSV_IMPORT_ERROR", "Malformed line: $line")
                        response = ImportCSVResponse(
                            errorMessage = "Malformed line: $line",
                            successMessage = "",
                            statusCode = 400,
                            passwords = importedPasswords
                        )
                        break
                    }
                }


                reader.close()
                fileInputStream.close()


                if (importedPasswords.isNotEmpty()) {
                    response = ImportCSVResponse(
                        errorMessage = "",
                        successMessage = "Successfully imported ${importedPasswords.size} passwords.",
                        statusCode = 200,
                        passwords = importedPasswords
                    )
                    Log.d("CSV_IMPORT", "Successfully imported ${importedPasswords.size} passwords.")
                } else {
                    response = ImportCSVResponse(
                        errorMessage = "No valid passwords imported.",
                        successMessage = "",
                        statusCode = 400,
                        passwords = importedPasswords
                    )
                }
            }
        }
    } catch (e: Exception) {
        response = ImportCSVResponse(
            errorMessage = "Error importing passwords from CSV: ${e.message}",
            successMessage = "",
            statusCode = 500,
            passwords = importedPasswords
        )
        Log.e("CSV_IMPORT_ERROR", "Error importing passwords from CSV", e)
    }

    return response
}

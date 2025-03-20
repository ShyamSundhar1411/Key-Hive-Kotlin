package com.axionlabs.keyhive.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.axionlabs.keyhive.model.ImportCSVResponse
import com.axionlabs.keyhive.model.Password
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.InputStreamReader
import java.util.Date

fun exportPasswordsToCSV(context: Context, passwords: List<Password>): Uri? {
    return try {

        val filename = "passwords" + System.currentTimeMillis() + ".csv"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }
        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                outputStream.write("App/Service,Username,Password,Description\n".toByteArray())
                passwords.forEach { password ->
                    val csvLine = "${password.type},${password.username}," +
                            "${CryptoUtils().decrypt(password.password)},${password.description}\n"
                    outputStream.write(csvLine.toByteArray())
                }
                outputStream.flush()
            }
        }

        Log.d("CSV_EXPORT", "Exported CSV at $uri")
        uri

    } catch (e: Exception) {
        e.printStackTrace()
        Log.e("CSV_EXPORT_ERROR", "Error exporting passwords to CSV", e)
        null

    }
}

fun shareCsvFile(context: Context, uri: Uri) {
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



fun getFileNameFromUri(context: Context, uri: Uri): String? {
    var name: String? = null
    context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                name = cursor.getString(nameIndex)
            }
        }
    }
    return name
}

fun importPasswordsFromCSV(context: Context, uri: Uri): ImportCSVResponse {
    val importedPasswords = mutableListOf<Password>()
    var response = ImportCSVResponse(
        errorMessage = "Unknown error occurred.",
        successMessage = "",
        statusCode = 500,
        passwords = importedPasswords
    )

    try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->

            val reader = BufferedReader(InputStreamReader(inputStream))
            val headerLine = reader.readLine()

            val expectedHeader = "App/Service,Username,Password,Description"

            Log.d("CSV_IMPORT_HEADER", headerLine ?: "null")

            // Validate header
            if (headerLine == null) {
                return ImportCSVResponse(
                    errorMessage = "Empty CSV file.",
                    successMessage = "",
                    statusCode = 400,
                    passwords = importedPasswords
                )
            }

            if (headerLine.trim() != expectedHeader.trim()) {
                return ImportCSVResponse(
                    errorMessage = "Invalid CSV format. Expected header: $expectedHeader",
                    successMessage = "",
                    statusCode = 400,
                    passwords = importedPasswords
                )
            }

            // Process CSV lines
            var line: String?
            var malformedLines = 0

            while (reader.readLine().also { line = it } != null) {
                val values = line!!.split(",").map { it.trim() }

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
                    malformedLines++
                    Log.e("CSV_IMPORT_ERROR", "Malformed line ignored: $line")
                }
            }

            reader.close()

            // Final response
            response = if (importedPasswords.isNotEmpty()) {
                ImportCSVResponse(
                    errorMessage = if (malformedLines > 0) "$malformedLines malformed line(s) ignored." else "",
                    successMessage = "Successfully imported ${importedPasswords.size} passwords.",
                    statusCode = 200,
                    passwords = importedPasswords
                )
            } else {
                ImportCSVResponse(
                    errorMessage = "No valid passwords imported.",
                    successMessage = "",
                    statusCode = 400,
                    passwords = importedPasswords
                )
            }

        } ?: run {
            response = ImportCSVResponse(
                errorMessage = "Failed to open CSV file.",
                successMessage = "",
                statusCode = 500,
                passwords = importedPasswords
            )
        }

    } catch (e: Exception) {
        Log.e("CSV_IMPORT_ERROR", "Error importing CSV", e)
        response = ImportCSVResponse(
            errorMessage = "Error importing CSV: ${e.message}",
            successMessage = "",
            statusCode = 500,
            passwords = importedPasswords
        )
    }

    return response
}

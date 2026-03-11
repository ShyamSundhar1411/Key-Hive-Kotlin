package com.axionlabs.keyhive.services

import android.os.CancellationSignal
import android.service.autofill.AutofillService
import android.service.autofill.Dataset
import android.service.autofill.FillCallback
import android.service.autofill.FillRequest
import android.service.autofill.FillResponse
import android.service.autofill.SaveCallback
import android.service.autofill.SaveRequest
import android.view.autofill.AutofillValue
import android.widget.RemoteViews
import com.axionlabs.keyhive.R
import com.axionlabs.keyhive.repository.PasswordDbRepository
import com.axionlabs.keyhive.utils.AutofillParser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class KeyHiveAutoFillService: AutofillService() {
    @Inject lateinit var repository: PasswordDbRepository
    override fun onFillRequest(request: FillRequest, cancellationSignal: CancellationSignal, callback: FillCallback) {
        val parser = AutofillParser()
        val structure = request.fillContexts.last().structure
        parser.parse(structure)
        val query = parser.domain ?: structure.activityComponent.packageName
        val passwords = runBlocking {
            repository.getAllPasswords()
        }.filter { it.username.contains(query, ignoreCase = true) || it.type.contains(query, ignoreCase = true) }
        val responseBuilder = FillResponse.Builder()

        passwords.forEach {
            password->
            val dataset = Dataset.Builder()
            val presentation = RemoteViews(packageName, R.layout.autofill_suggestion_item)
            presentation.setTextViewText(R.id.autofill_text, "KeyHive: ${password.username}")
            parser.usernameField?.autofillId?.let{
                id -> dataset.setValue(id, AutofillValue.forText(password.username),presentation)

            }
            parser.passwordField?.autofillId?.let { id ->
                dataset.setValue(id, AutofillValue.forText(password.password), presentation)
            }
            responseBuilder.addDataset(dataset.build())
        }
        callback.onSuccess(responseBuilder.build())
    }
    override fun onSaveRequest(request: SaveRequest, callback: SaveCallback) {

    }
}

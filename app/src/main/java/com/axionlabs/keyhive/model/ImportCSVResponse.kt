package com.axionlabs.keyhive.model

data class ImportCSVResponse(
    val errorMessage: String,
    val successMessage: String,
    val statusCode: Int,
    val passwords: List<Password>,
)

package com.david.pruebanoxos.retrofit.response

data class AuthorizationResponse(
    val receiptId: String?,
    val rrn: String?,
    val statusCode: String,
    val statusDescription: String
) {
}
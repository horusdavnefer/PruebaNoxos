package com.david.pruebanoxos.objectsDTO

data class TransactionDTO(
    val idTransaction: String,
    val commerceCode: String,
    val terminalCode: String,
    val amount: String,
    val card: String,
    var receiptId: String?,
    var rrn: String?,
    val authorization: Boolean,
    val annulation: Boolean
) {
}
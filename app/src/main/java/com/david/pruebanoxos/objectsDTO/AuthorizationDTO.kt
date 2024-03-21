package com.david.pruebanoxos.objectsDTO

data class AuthorizationDTO(
    val id: String,
    val commerceCode: String,
    val terminalCode: String,
    val amount: String,
    val card: String,
) {
}
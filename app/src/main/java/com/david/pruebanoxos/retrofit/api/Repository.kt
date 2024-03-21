package com.david.pruebanoxos.retrofit.api

import com.david.pruebanoxos.db.entities.TransaccionEntity
import com.david.pruebanoxos.objectsDTO.AnnulationDTO
import com.david.pruebanoxos.objectsDTO.AuthorizationDTO
import com.david.pruebanoxos.objectsDTO.TransactionDTO
import com.david.pruebanoxos.retrofit.response.AnnulationResponse
import com.david.pruebanoxos.retrofit.response.AuthorizationResponse

interface Repository {
    /**
     * Funcion que obtiene la respuesta de Authorization
     */
    suspend fun postAuthorization(authorizationKey: String, authorizationDTO: AuthorizationDTO): AuthorizationResponse

    /**
     * Funcion que obtiene la respuesta de Annulation
     */
    suspend fun postAnnulation(authorizationKey: String, annulationDTO: AnnulationDTO): AnnulationResponse

    /**
     * Funcion que inserta las transacciones
     */
    suspend fun insertTransaction(transactionDTO: TransactionDTO)

    /**
     * Funcion que trae las transacciones
     */
    suspend fun getAllTransactions():List<TransaccionEntity>

    /**
     * Funcion que actualiza la transaccion
     */
    suspend fun updateTransaction(transactionEntity: TransaccionEntity)
}
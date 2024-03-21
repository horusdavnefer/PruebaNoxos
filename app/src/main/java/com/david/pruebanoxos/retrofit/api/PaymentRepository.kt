package com.david.pruebanoxos.retrofit.api

import com.david.pruebanoxos.db.dao.TransaccionDao
import com.david.pruebanoxos.db.entities.TransaccionEntity
import com.david.pruebanoxos.objectsDTO.AnnulationDTO
import com.david.pruebanoxos.objectsDTO.AuthorizationDTO
import com.david.pruebanoxos.objectsDTO.TransactionDTO
import com.david.pruebanoxos.retrofit.response.AnnulationResponse
import com.david.pruebanoxos.retrofit.response.AuthorizationResponse
import javax.inject.Inject

class PaymentRepository  @Inject constructor(
    private val retrofitInstance: PaymentApi,
    private val appDao: TransaccionDao
): Repository {
    override suspend fun postAuthorization(
        authorizationKey: String,
        authorizationDTO: AuthorizationDTO
    ): AuthorizationResponse {
        return retrofitInstance.postAuthorization(authorizationKey = authorizationKey,authorizationDTO = authorizationDTO)
    }

    override suspend fun postAnnulation(
        authorizationKey: String,
        annulationDTO: AnnulationDTO
    ): AnnulationResponse {
        return retrofitInstance.postAnnulation(authorizationKey = authorizationKey, annulationDTO = annulationDTO)
    }



    override suspend fun insertTransaction(transactionDTO: TransactionDTO) {
        val transactionEntity = TransaccionEntity(
            idTransaction = transactionDTO.idTransaction,
            commerceCode = transactionDTO.commerceCode,
            terminalCode = transactionDTO.terminalCode,
            amount = transactionDTO.amount,
            card = transactionDTO.card,
            rrn = transactionDTO.rrn,
            receiptId = transactionDTO.receiptId,
            annulation = transactionDTO.annulation,
            authorization = transactionDTO.authorization
        )
        appDao.insert(transactionEntity)
    }

    override suspend fun getAllTransactions(): List<TransaccionEntity>{
        return appDao.getAll()
    }

    override suspend fun updateTransaction(transactionEntity: TransaccionEntity){
        return appDao.updateTransaction(transactionEntity)
    }


}
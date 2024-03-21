package com.david.pruebanoxos.retrofit.usecases

import com.david.pruebanoxos.objectsDTO.TransactionDTO
import com.david.pruebanoxos.retrofit.api.Repository
import javax.inject.Inject

class TransactionInsertUseCase @Inject constructor(
    private val repository: Repository
){
    suspend operator fun invoke(transactionDTO: TransactionDTO) {
        repository.insertTransaction(transactionDTO)

    }
}
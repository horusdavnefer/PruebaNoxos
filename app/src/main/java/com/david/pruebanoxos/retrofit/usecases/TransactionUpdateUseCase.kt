package com.david.pruebanoxos.retrofit.usecases

import com.david.pruebanoxos.db.entities.TransaccionEntity
import com.david.pruebanoxos.retrofit.api.Repository
import javax.inject.Inject

class TransactionUpdateUseCase @Inject constructor(
    private val repository: Repository
){
    suspend operator fun invoke(transaccionEntity: TransaccionEntity) {
        repository.updateTransaction(transaccionEntity)

    }
}
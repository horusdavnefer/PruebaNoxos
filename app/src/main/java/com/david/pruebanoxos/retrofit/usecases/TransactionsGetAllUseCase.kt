package com.david.pruebanoxos.retrofit.usecases

import com.david.pruebanoxos.retrofit.api.Repository
import com.david.pruebanoxos.utils.Constants.BAD_REQUEST
import com.david.pruebanoxos.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TransactionsGetAllUseCase @Inject constructor(
    private val repository: Repository
){
    operator fun invoke(): Flow<Resource<Any>> = flow {
        try {
            emit(Resource.Loading(null))
            val transactionsJson = repository.getAllTransactions()
            if (transactionsJson.isNotEmpty()){
                emit(Resource.Success(transactionsJson))
            }else{
                emit(Resource.Error(BAD_REQUEST.toString()))
            }
        }
        catch (e: Exception){
            emit(Resource.Error(BAD_REQUEST.toString()))
        }
    }
}
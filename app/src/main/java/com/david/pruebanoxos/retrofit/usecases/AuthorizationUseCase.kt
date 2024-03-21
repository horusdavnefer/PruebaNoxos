package com.david.pruebanoxos.retrofit.usecases

import com.david.pruebanoxos.objectsDTO.AuthorizationDTO
import com.david.pruebanoxos.retrofit.api.Repository
import com.david.pruebanoxos.retrofit.response.AuthorizationResponse
import com.david.pruebanoxos.utils.Constants.APPROVED
import com.david.pruebanoxos.utils.Constants.BAD_REQUEST
import com.david.pruebanoxos.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthorizationUseCase @Inject constructor(
    private val repository: Repository
){
    operator fun invoke(authorizationKey: String, authorizationDTO: AuthorizationDTO): Flow<Resource<Any>> = flow {
        try {
            emit(Resource.Loading(null))
            val authorizationJson = repository.postAuthorization(authorizationKey, authorizationDTO)
            val annulationResponse = AuthorizationResponse(receiptId = authorizationJson.receiptId, rrn = authorizationJson.rrn,
                statusCode = authorizationJson.statusCode, statusDescription = authorizationJson.statusDescription)
            if (authorizationJson.statusCode == APPROVED) {
                emit(Resource.Success(annulationResponse))
            } else {
                emit(Resource.Error(annulationResponse.statusDescription))
            }
        } catch (e: Exception){
            emit(Resource.Error(BAD_REQUEST.toString()))
        }
    }
}
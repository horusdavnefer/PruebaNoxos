package com.david.pruebanoxos.retrofit.usecases

import com.david.pruebanoxos.objectsDTO.AnnulationDTO
import com.david.pruebanoxos.retrofit.api.Repository
import com.david.pruebanoxos.retrofit.response.AnnulationResponse
import com.david.pruebanoxos.utils.Constants.APPROVED
import com.david.pruebanoxos.utils.Constants.BAD_REQUEST
import com.david.pruebanoxos.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnnulationUseCase @Inject constructor(
    private val repository: Repository
){
    operator fun invoke(authorizationKey: String, annulationDTO: AnnulationDTO): Flow<Resource<Any>> = flow {
        try {
            emit(Resource.Loading(null))
            val annulationJson = repository.postAnnulation(authorizationKey, annulationDTO)
            val annulationResponse = AnnulationResponse(statusCode = annulationJson.statusCode, statusDescription = annulationJson.statusDescription)
            if (annulationJson.statusCode == APPROVED) {
                emit(Resource.Success(annulationResponse))
            } else {
                emit(Resource.Error(annulationResponse.statusCode))
            }
        }
        catch (e: Exception){
            emit(Resource.Error(BAD_REQUEST.toString()))
        }
    }
}
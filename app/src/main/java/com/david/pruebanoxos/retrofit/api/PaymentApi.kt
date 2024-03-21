package com.david.pruebanoxos.retrofit.api

import com.david.pruebanoxos.objectsDTO.AnnulationDTO
import com.david.pruebanoxos.objectsDTO.AuthorizationDTO
import com.david.pruebanoxos.retrofit.response.AnnulationResponse
import com.david.pruebanoxos.retrofit.response.AuthorizationResponse
import com.david.pruebanoxos.utils.Constants
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PaymentApi {
    /**
     * Funcion que autoriza las transacciones
     */
    @POST(Constants.API_AUTHORIZATION)
    suspend fun postAuthorization(
        @Header("Authorization") authorizationKey: String,
        @Body authorizationDTO: AuthorizationDTO
    ): AuthorizationResponse

    /**
     * Funcion que anula las transacciones
     */
    @POST(Constants.API_ANNULATION)
    suspend fun postAnnulation(
        @Header("Authorization") authorizationKey: String,
        @Body annulationDTO: AnnulationDTO
    ): AnnulationResponse
}
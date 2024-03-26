package com.david.pruebanoxos.activities.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.david.pruebanoxos.db.entities.TransaccionEntity
import com.david.pruebanoxos.objectsDTO.AnnulationDTO
import com.david.pruebanoxos.objectsDTO.AuthorizationDTO
import com.david.pruebanoxos.objectsDTO.TransactionDTO
import com.david.pruebanoxos.retrofit.response.AnnulationResponse
import com.david.pruebanoxos.retrofit.response.AuthorizationResponse
import com.david.pruebanoxos.retrofit.usecases.AnnulationUseCase
import com.david.pruebanoxos.retrofit.usecases.AuthorizationUseCase
import com.david.pruebanoxos.retrofit.usecases.TransactionInsertUseCase
import com.david.pruebanoxos.retrofit.usecases.TransactionUpdateUseCase
import com.david.pruebanoxos.retrofit.usecases.TransactionsGetAllUseCase
import com.david.pruebanoxos.utils.DataState
import com.david.pruebanoxos.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val authorizationUseCase: AuthorizationUseCase,
    private val annulationUseCase: AnnulationUseCase,
    private val transactionInsertUseCase: TransactionInsertUseCase,
    private val transactionsUseCase: TransactionsGetAllUseCase,
    private val transactionUpdateUseCase: TransactionUpdateUseCase
): ViewModel(){

    private val _transactions = MutableLiveData<DataState<List<TransaccionEntity>>>(DataState())
    val transactions = _transactions

    private val _authorization = MutableLiveData<DataState<AuthorizationResponse>>(DataState())
    var authorization = _authorization

    private val _annulation = MutableLiveData<DataState<AnnulationResponse>>(DataState())
    val annulation = _annulation
    lateinit var transactionDTO: TransactionDTO
    var transactionIdSelected: Int? = null
    var transactionAnnulation: Boolean? = null



    fun getAuthorization(authorizationKey: String, authorizationDTO: AuthorizationDTO) {
        authorizationUseCase(authorizationKey, authorizationDTO).onEach { resources->
            when(resources){
                is Resource.Success -> _authorization.value = DataState(data = resources.data as AuthorizationResponse)
                is Resource.Loading -> _authorization.value = DataState(isLoading = true)
                is Resource.Error -> _authorization.value = DataState(error = resources.errorCode)
            }
        }.launchIn(viewModelScope)
    }

    fun getAnnulation(authorizationKey: String , annulationDTO: AnnulationDTO) {
        annulationUseCase(authorizationKey, annulationDTO).onEach { resources->
            when(resources){
                is Resource.Success -> _annulation.value = DataState(data = resources.data as AnnulationResponse)
                is Resource.Loading -> _annulation.value = DataState(isLoading = true)
                is Resource.Error -> _annulation.value = DataState(error = resources.errorCode)
            }
        }.launchIn(viewModelScope)
    }


    fun setTransactionInsert(transactionDTO: TransactionDTO) {
        viewModelScope.launch {
            transactionInsertUseCase.invoke(transactionDTO)
        }
    }


    fun getTransactionsList() {
        transactionsUseCase().onEach { resources ->
            when(resources){
                is Resource.Success -> _transactions.value = DataState(data = resources.data as List<TransaccionEntity>)
                is Resource.Loading -> _transactions.value = DataState(isLoading = true)
                is Resource.Error -> _transactions.value = DataState(error = resources.errorCode)
            }
        }.launchIn(viewModelScope)
    }

    fun setTransactionUpdate(transactionEntity: TransaccionEntity) {
        viewModelScope.launch {
            transactionUpdateUseCase.invoke(transactionEntity)
        }
    }

    fun setTransactionRoom(transactionDTO: TransactionDTO){
        this.transactionDTO = transactionDTO
    }


}
package com.example.tenretni.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenretni.core.ApiResult
import com.example.tenretni.data.repositories.GatewayRepository
import com.example.tenretni.data.repositories.NetworkRepository
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.domain.models.Network
import com.example.tenretni.ui.gateways.GatewaysUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NetworksViewModel : ViewModel() {

    private val networkRepository = NetworkRepository()
    private val _networkUiState = MutableStateFlow<NetworksUiState>(NetworksUiState.Loading)

    val networkUiState = _networkUiState.asStateFlow()

    fun loadNodes(){
        viewModelScope.launch {
            networkRepository.retrieveAll().collect{apiResult ->
                _networkUiState.update {
                    when(apiResult){
                        is ApiResult.Error -> NetworksUiState.Error(apiResult.exception)
                        ApiResult.Loading -> NetworksUiState.Loading
                        is ApiResult.Success ->{
                            NetworksUiState.Success(apiResult.data)
                        }
                    }
                }

            }
        }
    }

}
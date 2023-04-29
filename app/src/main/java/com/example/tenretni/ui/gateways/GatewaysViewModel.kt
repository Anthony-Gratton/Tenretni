package com.example.tenretni.ui.gateways

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tenretni.core.ApiResult
import com.example.tenretni.data.repositories.CustomerRepository
import com.example.tenretni.data.repositories.GatewayRepository
import com.example.tenretni.data.repositories.TicketRepository
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.ui.customer.CustomerUiState
import com.example.tenretni.ui.tickets.detail.DetailTicketUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GatewaysViewModel : ViewModel() {

    private val gatewayRepository = GatewayRepository()
    private val _gatewayUiState = MutableStateFlow<GatewaysUiState>(GatewaysUiState.Loading)
    private var gatewayList: MutableList<Gateway> = mutableListOf()

    val gatewaysUiState = _gatewayUiState.asStateFlow()

init {

}


    fun loadGateways(){
        viewModelScope.launch {
            gatewayRepository.retrieveAll().collect{apiResult ->
                _gatewayUiState.update {
                    when(apiResult){
                        is ApiResult.Error -> GatewaysUiState.Error(apiResult.exception)
                        ApiResult.Loading -> GatewaysUiState.Loading
                        is ApiResult.Success ->{
                            gatewayList = apiResult.data.toMutableList()
                            GatewaysUiState.Success(apiResult.data)
                        }
                    }
                }

            }
        }
    }

}
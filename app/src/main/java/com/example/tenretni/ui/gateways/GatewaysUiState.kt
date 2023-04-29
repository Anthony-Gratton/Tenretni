package com.example.tenretni.ui.gateways

import com.example.tenretni.domain.models.Gateway


sealed class GatewaysUiState{
    object Loading: GatewaysUiState()
    class Success(val gateways: List<Gateway>): GatewaysUiState()
    class Error(val exception: Exception? = null) : GatewaysUiState()
}

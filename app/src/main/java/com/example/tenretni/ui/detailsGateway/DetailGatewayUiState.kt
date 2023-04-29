package com.example.tenretni.ui.detailsGateway

import com.example.tenretni.domain.models.Gateway

sealed class DetailGatewayUiState {
    object Loading: DetailGatewayUiState()
    class Success(val gateway: Gateway): DetailGatewayUiState()
    class Error(val exception: Exception? = null) : DetailGatewayUiState()
}
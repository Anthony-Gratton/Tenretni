package com.example.tenretni.ui.notifications
import com.example.tenretni.domain.models.Network
import org.w3c.dom.Node

sealed class NetworksUiState{
    object Loading: NetworksUiState()
    class Success(val network: Network): NetworksUiState()
    class Error(val exception: Exception? = null) : NetworksUiState()
}
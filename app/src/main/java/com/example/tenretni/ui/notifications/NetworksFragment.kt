package com.example.tenretni.ui.notifications


import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tenretni.R
import com.example.tenretni.core.DateHelper
import com.example.tenretni.databinding.FragmentGatewaysBinding
import com.example.tenretni.databinding.FragmentNetworksBinding
import com.example.tenretni.ui.gateways.GatewayRecyclerViewAdapter
import com.example.tenretni.ui.gateways.GatewaysUiState
import com.example.tenretni.ui.gateways.GatewaysViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class NetworksFragment : Fragment(R.layout.fragment_networks) {

    private val binding: FragmentNetworksBinding by viewBinding()
    private val viewModel: NetworksViewModel by viewModels()

    private lateinit var networksRecyclerViewAdapter: NetworksRecyclerViewAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networksRecyclerViewAdapter = NetworksRecyclerViewAdapter(listOf())
        viewModel.loadNodes()

        binding.rcvNetworks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = networksRecyclerViewAdapter
        }

        viewModel.networkUiState.onEach {
            when(it){
                is NetworksUiState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                }
                NetworksUiState.Loading ->Unit
                is NetworksUiState.Success -> {
                    binding.txvLastUpdate.text= getString(R.string.txtUpdateNetwork, DateHelper.formatISODate(it.network.updateDate))
                    binding.txvNextReboot.text=getString(R.string.txtNextRebootNetwork, DateHelper.formatISODate(it.network.nextReboot))
                    networksRecyclerViewAdapter.nodes = it.network.nodes
                    networksRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }



}
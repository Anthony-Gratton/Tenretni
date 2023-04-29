package com.example.tenretni.ui.gateways

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tenretni.R
import com.example.tenretni.databinding.FragmentGatewaysBinding
import com.example.tenretni.domain.models.Gateway
import com.example.tenretni.ui.tickets.detail.DetailTicketFragmentDirections
import com.example.tenretni.ui.tickets.detail.DetailTicketRecyclerViewAdapter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GatewaysFragment : Fragment(R.layout.fragment_gateways) {

    private val binding: FragmentGatewaysBinding by viewBinding()
    private val viewModel: GatewaysViewModel by viewModels()

    private lateinit var gatewaysRecyclerViewAdapter: GatewayRecyclerViewAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gatewaysRecyclerViewAdapter = GatewayRecyclerViewAdapter(listOf(), ::onRecyclerViewGateWayClick)
        viewModel.loadGateways()

        binding.rcvGateways.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = gatewaysRecyclerViewAdapter
        }

        viewModel.gatewaysUiState.onEach {
            when(it){
                is GatewaysUiState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                }
                GatewaysUiState.Loading -> {
                    binding.rcvGateways.visibility = View.GONE
                    binding.pgbLoading.visibility = View.VISIBLE
                }
                is GatewaysUiState.Success -> {
                    binding.rcvGateways.visibility = View.VISIBLE
                    binding.pgbLoading.visibility = View.GONE

                    gatewaysRecyclerViewAdapter.gateways = it.gateways
                    gatewaysRecyclerViewAdapter.notifyDataSetChanged()
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }




    private fun onRecyclerViewGateWayClick(gateway: Gateway){
        val action = GatewaysFragmentDirections.actionNavigationGatewaysToDetailGatewayFragment(gateway.href)
        findNavController().navigate(action)
    }






}
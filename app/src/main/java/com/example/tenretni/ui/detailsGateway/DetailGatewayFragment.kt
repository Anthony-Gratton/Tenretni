package com.example.tenretni.ui.detailsGateway

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.TranslationHelper
import com.example.tenretni.databinding.FragmentDetailGatewayBinding
import com.example.tenretni.domain.models.Gateway
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


class DetailGatewayFragment : Fragment(R.layout.fragment_detail_gateway) {

    private val args: DetailGatewayFragmentArgs by navArgs()
    private lateinit var currentGateway: Gateway
    private val binding: FragmentDetailGatewayBinding by viewBinding()
    private val viewModel: DetailGatewayViewModel by viewModels{
        DetailGatewayViewModel.Factory(args.href)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdate.setOnClickListener{
            viewModel.actionGateway("update")
        }
        binding.btnReboot.setOnClickListener{
            viewModel.actionGateway("reboot")
        }


        viewModel.detailGatewayUiState.onEach {
            when(it){
                is DetailGatewayUiState.Error -> {
                    Toast.makeText(requireContext(), it.exception?.localizedMessage, Toast.LENGTH_LONG).show()
                    requireActivity().supportFragmentManager.popBackStack()
                }
                DetailGatewayUiState.Loading -> Unit
                is DetailGatewayUiState.Success -> {
                    currentGateway = it.gateway
                    bindContent(it.gateway)
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun onResume() {
        viewModel.loadDetailGateway()
        super.onResume()
    }

    @SuppressLint("SetTextI18n")
    fun bindContent(gateway: Gateway){
        (requireActivity() as AppCompatActivity).supportActionBar?.title = requireContext().getString(R.string.gatewayId, gateway.serialNumber)
        binding.chipStatus.chipBackgroundColor = ColorHelper.connectionStatusColor(requireContext(), gateway.connection.status)
        binding.chipStatus.text = TranslationHelper.connectionStatusString(requireContext(), gateway.connection.status)
        binding.txvUuid.text = gateway.serialNumber
        binding.txvMac.text = gateway.config.mac
        binding.txvSsid.text = "SSID: ${gateway.config.SSID}"
        binding.txvPin.text = "PIN: ${gateway.pin}"
        binding.txvIp.text = gateway.connection.ip
        binding.txvDetailPing.text = requireContext().getString(R.string.txtPing, gateway.connection.ping)
        binding.txvDetailDownload.text = requireContext().getString(R.string.txtSpeed, gateway.connection.download)
        binding.txvDetailUpload.text = requireContext().getString(R.string.txtSpeed, gateway.connection.upload)
        binding.txvSignal.text = requireContext().getString(R.string.txtSignal, gateway.connection.signal)
        binding.txvHash.text = gateway.hash
        binding.txvKernel.text = "Kernel revision ${gateway.config.kernelRevision}"
        binding.txvVersion.text = "Version ${gateway.config.version}"
        //TODO: Check image for kernel
        bindImage(0, binding.imvElement1)
        bindImage(1, binding.imvElement2)
        bindImage(2, binding.imvElement3)
        bindImage(3, binding.imvElement4)
        bindImage(4, binding.imvElement5)

        if (gateway.connection.status == "Online"){
            binding.btnUpdate.visibility = View.VISIBLE
            binding.btnReboot.visibility = View.VISIBLE
            binding.txvGatewayNA.visibility = View.GONE
            binding.grpDetailGateway.visibility = View.VISIBLE
        }
        else{
            binding.btnUpdate.visibility = View.GONE
            binding.btnReboot.visibility = View.GONE
            binding.grpDetailGateway.visibility = View.GONE
            binding.txvGatewayNA.visibility = View.VISIBLE
        }

    }

fun bindImage( kernelNumber: Int, image:ImageView ){
    val res = resources
    val mDrawableName = "element_${currentGateway.config.kernel[kernelNumber]}".lowercase()
    val resID = res.getIdentifier(mDrawableName, "drawable", requireContext().packageName)
    var drawable = res.getDrawable(resID)
    image.setImageDrawable(drawable)
}


}
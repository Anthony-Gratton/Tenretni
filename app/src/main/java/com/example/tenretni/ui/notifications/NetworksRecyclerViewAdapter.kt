package com.example.tenretni.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tenretni.R
import com.example.tenretni.core.ColorHelper
import com.example.tenretni.core.TranslationHelper
import com.example.tenretni.databinding.ItemNetworkBinding
import com.example.tenretni.domain.models.Network
import com.example.tenretni.domain.models.NetworkNode
import com.example.tenretni.ui.gateways.GatewayRecyclerViewAdapter
import org.w3c.dom.Node

class NetworksRecyclerViewAdapter (

    var nodes: List<NetworkNode> = listOf()) : RecyclerView.Adapter<NetworksRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworksRecyclerViewAdapter.ViewHolder {
        return ViewHolder(ItemNetworkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, ) {
        val node = nodes[position]
        holder.bind(node)

    }

    override fun getItemCount() = nodes.size

    inner class ViewHolder(private val binding: ItemNetworkBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(node: NetworkNode){
            binding.chipNetworkStatus.chipBackgroundColor = ColorHelper.connectionStatusColor(itemView.context, node.connection.status)
            binding.chipNetworkStatus.text = TranslationHelper.connectionStatusString(itemView.context, node.connection.status)
            binding.txvNameNetwork.text = node.name
            if(node.connection.status == "Online"){
                binding.txvNodeNA.visibility = View.GONE
                binding.grpNodeOnline.visibility = View.VISIBLE
                binding.txvPingNetwork.text = itemView.context.getString(R.string.txtPing, node.connection.ping)
                binding.txvSignalNetwork.text = itemView.context.getString(R.string.txtSignal, node.connection.signal)
                binding.txvDownloadNetwork.text = itemView.context.getString(R.string.txtSpeed, node.connection.download)
                binding.txvUploadNetwork.text = itemView.context.getString(R.string.txtSpeed, node.connection.upload)
            }
            else{
                binding.txvNodeNA.visibility = View.VISIBLE
                binding.grpNodeOnline.visibility = View.GONE
            }
        }
    }

}
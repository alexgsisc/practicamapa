package com.example.practicamaps.maps.presentation.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.practicamaps.databinding.DetailListItemBinding
import com.example.practicamaps.maps.data.entity.RoutersModel

class ListRouterMapsAdapter(val viewAdapter: ListRouterMapsAdapterView) :
    RecyclerView.Adapter<ListRouterMapsAdapter.MapsViewHolder>() {

    private var listRouter: List<RoutersModel> = listOf()

    fun setRouters(list: List<RoutersModel>) {
        this.listRouter = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapsViewHolder {
        val binding =
            DetailListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MapsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MapsViewHolder, position: Int) {
        var data: RoutersModel = listRouter[position]
        holder.binding.tvName.text = data.nameRouter
        holder.binding.tvDate.text = data.date
        holder.binding.root.setOnClickListener {
            Log.e("OnClick", "OnClick list")
            viewAdapter.setOnViewClickItem(data)
        }
    }

    override fun getItemCount(): Int {
        return listRouter.size
    }

    inner class MapsViewHolder(val binding: DetailListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface ListRouterMapsAdapterView {
        fun setOnViewClickItem(result: RoutersModel)
    }
}
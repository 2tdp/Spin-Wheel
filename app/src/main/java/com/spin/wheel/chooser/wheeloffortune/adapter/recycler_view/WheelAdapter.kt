package com.spin.wheel.chooser.wheeloffortune.adapter.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.spin.wheel.chooser.wheeloffortune.databinding.ItemWheelConfigBinding
import com.spin.wheel.chooser.wheeloffortune.entity.WheelModel
import com.spin.wheel.chooser.wheeloffortune.extensions.afterTextChanged
import com.spin.wheel.chooser.wheeloffortune.extensions.gone
import com.spin.wheel.chooser.wheeloffortune.extensions.onAvoidDoubleClick
import com.spin.wheel.chooser.wheeloffortune.extensions.visible

class WheelAdapter : Adapter<WheelAdapter.WheelVH>() {

    var listData: MutableList<WheelModel> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<WheelModel>) {
        listData.clear()
        listData.addAll(data)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun remove(item: WheelModel, pos: Int) {
        val position = listData.indexOf(item)
        listData.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun add(model: WheelModel) {
        listData.add(model)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WheelVH {
        return WheelVH(
            ItemWheelConfigBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: WheelVH, position: Int) {
        val item = listData[position]
        holder.onBind(item, position)
    }

    inner class WheelVH(val binding: ItemWheelConfigBinding) : ViewHolder(binding.root) {
        fun onBind(item: WheelModel, position: Int) {
            binding.edtName.setText(item.name)
            binding.edtName.afterTextChanged {
                listData[adapterPosition].name = it
            }
            if (item.isDelete) binding.imvDelete.visible() else binding.imvDelete.gone()

            binding.imvDelete.onAvoidDoubleClick {
                remove(item, position)
            }
        }
    }

    fun getListName(): ArrayList<String> = listData.map { it.name } as ArrayList<String>
}
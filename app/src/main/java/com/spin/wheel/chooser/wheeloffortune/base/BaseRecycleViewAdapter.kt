package com.spin.wheel.chooser.wheeloffortune.base

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseRecycleViewAdapter: Adapter<ViewHolder>() {

    private val listData: MutableList<Any> = mutableListOf()
    var onLoadMore: () -> Unit = {}
    private var isLoading = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return getCreateViewHolder(parent, viewType)!!
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listData[position]
        bindView(holder, item, position)

        if (!isLoading && position == listData.size - 1) {
            isLoading = true
            onLoadMore()
        }
    }

    abstract fun getCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder?
    abstract fun bindView(holder: ViewHolder, model: Any, position: Int)

    @SuppressLint("NotifyDataSetChanged")
    open fun refresh(data: List<Any>) {
        listData.clear()
        listData.addAll(data)
        notifyDataSetChanged()
        isLoading = false
    }


    open fun addModels(data: List<Any>) {
        listData.addAll(data)
        notifyItemRangeChanged(listData.size - data.size, listData.size - 1)
        isLoading = false
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun removeModels(model: Any) {
        try {
            listData.remove(model)
            notifyDataSetChanged()
        } catch (_: Exception) {}
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun removeModels(list: List<Any>) {
        listData.remove(list)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun removeModels(model: Any, position: Int) {
        try {
            listData.removeAt(position)
            notifyItemChanged(position)
        } catch (_: Exception) {}
    }


    @SuppressLint("NotifyDataSetChanged")
    open fun addModels(data: Any) {
        try {
            listData.add(data)
            notifyDataSetChanged()
            isLoading = false
        } catch (_: Exception) {}
    }

    @SuppressLint("NotifyDataSetChanged")
    open fun clear() {
        listData.clear()
        notifyDataSetChanged()
        isLoading = false
    }

    open fun checkAddExample(model: Any) {
        if (this.itemCount == 0) {
            addModels(model)
        }
    }

    open fun updateModel(model: Any, position: Int) {
        try {
            listData[position] = model
        } catch (_: Exception) {}
    }

    open val _listData = listData

    open fun getData() = listData

}
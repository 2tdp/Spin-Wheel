package com.spin.wheel.chooser.wheeloffortune.adapter.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spin.wheel.chooser.wheeloffortune.R
import com.spin.wheel.chooser.wheeloffortune.databinding.LayoutItemLanguageBinding
import com.spin.wheel.chooser.wheeloffortune.entity.LanguageModel
import com.spin.wheel.chooser.wheeloffortune.extensions.loadIcon

class LanguageAdapter : RecyclerView.Adapter<LanguageAdapter.LanguageHolder>() {

    private var listLanguage = mutableListOf<LanguageModel>()
    var onClick: (LanguageModel) -> Unit = {}
    private var isSelect = false

    @SuppressLint("NotifyDataSetChanged")
    fun setData(listLanguage: MutableList<LanguageModel>) {
        this.listLanguage = listLanguage

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageHolder {
        return LanguageHolder(
            LayoutItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = if (listLanguage.isNotEmpty()) listLanguage.size else 0
    override fun onBindViewHolder(holder: LanguageHolder, position: Int) {
        holder.onBind(position)
    }

    inner class LanguageHolder(val binding: LayoutItemLanguageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val item = listLanguage[position]

            if (!isSelect) {
                binding.imvLanguage.loadIcon(item.icon)
                binding.tvLvLanguage.text = item.name
            }

            if (item.isSelected) binding.llItem.setBackgroundResource(R.drawable.bg_item_language_selected)
            else binding.llItem.setBackgroundResource(R.drawable.bg_item_language)

            binding.llItem.setOnClickListener {
                isSelect = true
                initUi(position)
                onClick(item)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initUi(position: Int) {
        listLanguage.forEach { item -> item.isSelected = false }
        listLanguage[position].isSelected = true
        notifyDataSetChanged()
    }
}
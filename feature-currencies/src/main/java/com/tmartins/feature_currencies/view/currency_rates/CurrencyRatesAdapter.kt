package com.tmartins.feature_currencies.view.currency_rates

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tmartins.feature_currencies.databinding.ItemCurrencyValueBinding
import com.tmartins.feature_currencies.view.CurrencyValueUiModel

internal class CurrencyRatesAdapter
    : ListAdapter<CurrencyValueUiModel, CurrencyRatesAdapter.ViewHolder>(CurrenciesItemDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCurrencyValueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemCurrencyValueBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CurrencyValueUiModel) {
            binding.apply {
                currencyCode.text = item.code
                currencyValue.text = "${item.value}"
            }
        }
    }

    private object CurrenciesItemDiffUtil : DiffUtil.ItemCallback<CurrencyValueUiModel>() {

        override fun areItemsTheSame(
            oldItem: CurrencyValueUiModel,
            newItem: CurrencyValueUiModel,
        ): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(
            oldItem: CurrencyValueUiModel,
            newItem: CurrencyValueUiModel,
        ): Boolean {
            return oldItem == newItem
        }
    }

}
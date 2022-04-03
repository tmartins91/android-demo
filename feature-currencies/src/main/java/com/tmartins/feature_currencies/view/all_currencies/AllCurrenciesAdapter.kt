package com.tmartins.feature_currencies.view.all_currencies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tmartins.feature_currencies.databinding.ItemCurrencyBinding
import com.tmartins.feature_currencies.view.CurrencyUiModel

internal fun interface ItemClickListener {
    fun onItemClicked(item: CurrencyUiModel)
}

internal class AllCurrenciesAdapter(
    private val onItemClickListener: ItemClickListener
) : ListAdapter<CurrencyUiModel, AllCurrenciesAdapter.ViewHolder>(CurrenciesItemDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCurrencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onItemClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemCurrencyBinding,
        private val onItemClickListener: ItemClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CurrencyUiModel) {
            binding.apply {
                currencyName.text = item.name
                root.setOnClickListener { onItemClickListener.onItemClicked(item) }
            }
        }
    }

    private object CurrenciesItemDiffUtil : DiffUtil.ItemCallback<CurrencyUiModel>() {

        override fun areItemsTheSame(
            oldItem: CurrencyUiModel,
            newItem: CurrencyUiModel,
        ): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(
            oldItem: CurrencyUiModel,
            newItem: CurrencyUiModel,
        ): Boolean {
            return oldItem == newItem
        }
    }

}
package com.tmartins.feature_currencies.view.currency_rates

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tmartins.feature_currencies.databinding.ActivityCurrencyRatesBinding
import com.tmartins.feature_currencies.navigation.CurrenciesNavigatorDefault.Companion.BASE_CURRENCY
import com.tmartins.feature_currencies.view.currency_rates.CurrencyRatesViewModel.Event.ShowErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
internal class CurrencyRatesActivity : AppCompatActivity() {

    private val viewModel by viewModels<CurrencyRatesViewModel>()
    private lateinit var binding: ActivityCurrencyRatesBinding

    private val adapter = CurrencyRatesAdapter()

    private var eventJob: Job? = null
    private var stateJob: Job? = null

    private val baseCurrency: String by lazy { intent.getStringExtra(BASE_CURRENCY)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencyRatesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        observeEvent()
        observeState()

        lifecycleScope.launchWhenStarted { viewModel.onFirstLaunch(baseCurrency) }
    }

    override fun onDestroy() {
        eventJob?.cancel()
        stateJob?.cancel()
        super.onDestroy()
    }

    private fun observeEvent() {
        eventJob = lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is ShowErrorDialog -> showErrorDialog(
                        title = event.title,
                        message = event.message,
                        buttonText = event.buttonText,
                        action = event.action
                    )
                }
            }
        }
    }

    private fun observeState() {
        stateJob = lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                binding.currencyRatesLoading.isVisible = state.loading
                binding.currencyRatesDate.text = state.date
                adapter.submitList(state.currencies)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.currencyRatesRecyclerView.adapter = adapter
    }

    private fun showErrorDialog(
        title: String,
        message: String,
        buttonText: String,
        action: (String) -> Unit
    ) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonText) { _, _ -> action.invoke(baseCurrency) }
            .show()
    }
}
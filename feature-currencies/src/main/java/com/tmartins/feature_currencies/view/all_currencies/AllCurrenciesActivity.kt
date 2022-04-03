package com.tmartins.feature_currencies.view.all_currencies

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.tmartins.feature_currencies.databinding.ActivityAllCurrenciesBinding
import com.tmartins.feature_currencies.navigation.CurrenciesNavigator
import com.tmartins.feature_currencies.view.all_currencies.AllCurrenciesViewModel.Event.ShowErrorDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
internal class AllCurrenciesActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: CurrenciesNavigator

    private val viewModel by viewModels<AllCurrenciesViewModel>()
    private lateinit var binding: ActivityAllCurrenciesBinding

    private val adapter = AllCurrenciesAdapter {
        navigator.start(this, it.code)
    }

    private var eventJob: Job? = null
    private var stateJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllCurrenciesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeEvent()
        observeState()

        lifecycleScope.launchWhenStarted { viewModel.onFirstLaunch() }
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
                binding.allCurrenciesLoading.isVisible = state.loading
                adapter.submitList(state.currenciesList)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.allCurrenciesRecyclerView.adapter = adapter
    }

    private fun showErrorDialog(
        title: String,
        message: String,
        buttonText: String,
        action: () -> Unit
    ) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(buttonText) { _, _ -> action.invoke() }
            .show()
    }
}
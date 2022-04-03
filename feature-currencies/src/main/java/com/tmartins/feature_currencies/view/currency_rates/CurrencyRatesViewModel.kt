package com.tmartins.feature_currencies.view.currency_rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmartins.core_resources.StringResourceWrapper
import com.tmartins.core_resources.di.IoDispatcher
import com.tmartins.feature_currencies.R
import com.tmartins.feature_currencies.business.GetAllCurrenciesByBaseUseCase
import com.tmartins.feature_currencies.business.GetCurrenciesBaseResult
import com.tmartins.feature_currencies.view.CurrencyValueUiModel
import com.tmartins.feature_currencies.view.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class CurrencyRatesViewModel @Inject constructor(
    private val getAllCurrenciesByBaseUseCase: GetAllCurrenciesByBaseUseCase,
    private val strings: StringResourceWrapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state = MutableStateFlow(
        State(
            loading = false,
            date = "",
            currencies = emptyList()
        )
    )
    val state: Flow<State> = _state

    private val _events = MutableSharedFlow<Event>()
    val events: Flow<Event> = _events

    fun onFirstLaunch(baseCurrency: String) = viewModelScope.launch(dispatcher) {
        getAllCurrenciesRates(baseCurrency)
    }

    private suspend fun getAllCurrenciesRates(baseCurrency: String) {
        _state.value = _state.value.copy(loading = true)
        when (val result = getAllCurrenciesByBaseUseCase(baseCurrency)) {
            GetCurrenciesBaseResult.ServerError -> {
                _state.value = _state.value.copy(loading = false)
                _events.emit(
                    Event.ShowErrorDialog(
                        strings.getString(R.string.dialog_server_error_title),
                        strings.getString(R.string.dialog_server_error_body),
                        strings.getString(R.string.dialog_server_error_retry),
                        ::onFirstLaunch
                    )
                )
            }
            GetCurrenciesBaseResult.NoInternet -> {
                _state.value = _state.value.copy(loading = false)
                _events.emit(
                    Event.ShowErrorDialog(
                        strings.getString(R.string.dialog_no_internet_title),
                        strings.getString(R.string.dialog_no_internet_body),
                        strings.getString(R.string.dialog_no_internet_retry),
                        ::onFirstLaunch
                    )
                )
            }
            is GetCurrenciesBaseResult.Success -> {
                _state.value = _state.value.copy(
                    loading = false,
                    date = result.currencyBase.date,
                    currencies = result.currencyBase.currencies.map { it.toUi() }
                )
            }
        }
    }

    internal data class State(
        val loading: Boolean,
        val date: String,
        val currencies: List<CurrencyValueUiModel>
    )

    internal sealed class Event {
        data class ShowErrorDialog(
            val title: String,
            val message: String,
            val buttonText: String,
            val action: (String) -> Unit
        ) : Event()
    }
}
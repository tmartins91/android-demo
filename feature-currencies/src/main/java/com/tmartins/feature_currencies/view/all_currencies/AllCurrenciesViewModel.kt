package com.tmartins.feature_currencies.view.all_currencies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tmartins.core_resources.StringResourceWrapper
import com.tmartins.core_resources.di.IoDispatcher
import com.tmartins.feature_currencies.R
import com.tmartins.feature_currencies.business.GetAllCurrenciesUseCase
import com.tmartins.feature_currencies.business.GetCurrenciesResult
import com.tmartins.feature_currencies.view.CurrencyUiModel
import com.tmartins.feature_currencies.view.toUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AllCurrenciesViewModel @Inject constructor(
    private val getAllCurrenciesUseCase: GetAllCurrenciesUseCase,
    private val strings: StringResourceWrapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _state = MutableStateFlow(
        State(
            loading = false,
            currenciesList = emptyList()
        )
    )
    val state: Flow<State> = _state

    private val _events = MutableSharedFlow<Event>()
    val events: Flow<Event> = _events

    fun onFirstLaunch() = viewModelScope.launch(dispatcher) {
        getAllCurrencies()
    }

    private suspend fun getAllCurrencies() {
        _state.value = _state.value.copy(loading = true)
        when (val result = getAllCurrenciesUseCase()) {
            GetCurrenciesResult.ServerError -> {
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
            GetCurrenciesResult.NoInternet -> {
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
            is GetCurrenciesResult.Success -> {
                _state.value = _state.value.copy(
                    loading = false,
                    currenciesList = result.currencies.map { it.toUi() }
                )
            }
        }
    }

    internal data class State(
        val loading: Boolean,
        val currenciesList: List<CurrencyUiModel>
    )

    internal sealed class Event {
        data class ShowErrorDialog(
            val title: String,
            val message: String,
            val buttonText: String,
            val action: () -> Unit
        ) : Event()
    }
}
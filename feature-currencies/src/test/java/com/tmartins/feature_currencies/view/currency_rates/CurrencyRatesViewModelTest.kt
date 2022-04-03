package com.tmartins.feature_currencies.view.currency_rates

import com.tmartins.core_resources.StringResourceWrapper
import com.tmartins.feature_currencies.R
import com.tmartins.feature_currencies.ViewModelFlowCollector
import com.tmartins.feature_currencies.business.CurrencyBaseRatesResult
import com.tmartins.feature_currencies.business.CurrencyBaseResult
import com.tmartins.feature_currencies.business.GetAllCurrenciesByBaseUseCase
import com.tmartins.feature_currencies.business.GetCurrenciesBaseResult
import com.tmartins.feature_currencies.view.CurrencyValueUiModel
import com.tmartins.feature_currencies.view.currency_rates.CurrencyRatesViewModel.Event
import com.tmartins.feature_currencies.view.currency_rates.CurrencyRatesViewModel.State
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

private const val MOCK_DIALOG_TITLE = "title"
private const val MOCK_DIALOG_MESSAGE = "message"
private const val MOCK_DIALOG_BUTTON_TEXT = "retry"
private const val MOCK_DIALOG_TITLE_SERVER_ERROR = "title server error"
private const val MOCK_DIALOG_MESSAGE_SERVER_ERROR = "message server error"
private const val MOCK_DIALOG_BUTTON_TEXT_SERVER_ERROR = "retry server error"

@ExperimentalCoroutinesApi
internal class CurrencyRatesViewModelTest {

    private val getAllCurrenciesByBaseUseCase = mockk<GetAllCurrenciesByBaseUseCase>()
    private val strings = mockk<StringResourceWrapper>()
    private val dispatcher = TestCoroutineDispatcher()

    private val viewModel = CurrencyRatesViewModel(
        getAllCurrenciesByBaseUseCase = getAllCurrenciesByBaseUseCase,
        strings = strings,
        dispatcher = dispatcher
    )
    private lateinit var collector: ViewModelFlowCollector<State, Event>

    @Before
    fun setUp() {
        collector = ViewModelFlowCollector(viewModel.state, viewModel.events, dispatcher)

        every { strings.getString(R.string.dialog_no_internet_title) } returns MOCK_DIALOG_TITLE
        every { strings.getString(R.string.dialog_no_internet_body) } returns MOCK_DIALOG_MESSAGE
        every { strings.getString(R.string.dialog_no_internet_retry) } returns MOCK_DIALOG_BUTTON_TEXT

        every { strings.getString(R.string.dialog_server_error_title) } returns MOCK_DIALOG_TITLE_SERVER_ERROR
        every { strings.getString(R.string.dialog_server_error_body) } returns MOCK_DIALOG_MESSAGE_SERVER_ERROR
        every { strings.getString(R.string.dialog_server_error_retry) } returns MOCK_DIALOG_BUTTON_TEXT_SERVER_ERROR
    }

    @Test
    fun `GIVEN GetAllCurrenciesByBaseUseCase is Success WHEN the view first launch THEN state is correct`() =
        collector.test { states, _ ->
            val base = "GBP"
            coEvery { getAllCurrenciesByBaseUseCase(base) } returns GetCurrenciesBaseResult.Success(
                CurrencyBaseResult(
                    date = "2021-04-01",
                    currencies = listOf(
                        CurrencyBaseRatesResult("EUR", 2.0),
                        CurrencyBaseRatesResult("USD", 3.0)
                    )
                )
            )

            viewModel.onFirstLaunch(base)

            val expectedStates = listOf(
                State(false, "", emptyList()),
                State(true, "", emptyList()),
                State(
                    false,
                    "2021-04-01",
                    listOf(
                        CurrencyValueUiModel("EUR", 2.0),
                        CurrencyValueUiModel("USD", 3.0),
                    )
                )
            )
            assertEquals(expectedStates, states)
        }

    @Test
    fun `GIVEN GetAllCurrenciesByBaseUseCase is NoInternet WHEN the view first launch THEN state is correct`() =
        collector.test { states, events ->
            val base = "GBP"
            coEvery { getAllCurrenciesByBaseUseCase(base) } returns GetCurrenciesBaseResult.NoInternet

            viewModel.onFirstLaunch(base)

            val expectedStates = listOf(
                State(false, "", emptyList()),
                State(true, "", emptyList()),
                State(false, "", emptyList())
            )
            assertEquals(expectedStates, states)

            val expectedEvents = listOf(
                Event.ShowErrorDialog(
                    strings.getString(R.string.dialog_no_internet_title),
                    strings.getString(R.string.dialog_no_internet_body),
                    strings.getString(R.string.dialog_no_internet_retry),
                    viewModel::onFirstLaunch
                )
            )
            assertEquals(expectedEvents, events)
        }

    @Test
    fun `GIVEN GetAllCurrenciesByBaseUseCase is ServerError WHEN the view first launch THEN state is correct`() =
        collector.test { states, events ->
            val base = "GBP"
            coEvery { getAllCurrenciesByBaseUseCase(base) } returns GetCurrenciesBaseResult.ServerError

            viewModel.onFirstLaunch(base)

            val expectedStates = listOf(
                State(false, "", emptyList()),
                State(true, "", emptyList()),
                State(false, "", emptyList())
            )
            assertEquals(expectedStates, states)

            val expectedEvents = listOf(
                Event.ShowErrorDialog(
                    strings.getString(R.string.dialog_server_error_title),
                    strings.getString(R.string.dialog_server_error_body),
                    strings.getString(R.string.dialog_server_error_retry),
                    viewModel::onFirstLaunch
                )
            )
            assertEquals(expectedEvents, events)
        }
}
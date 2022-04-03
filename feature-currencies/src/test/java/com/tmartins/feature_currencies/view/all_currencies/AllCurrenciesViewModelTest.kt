package com.tmartins.feature_currencies.view.all_currencies

import com.tmartins.core_resources.StringResourceWrapper
import com.tmartins.feature_currencies.R
import com.tmartins.feature_currencies.ViewModelFlowCollector
import com.tmartins.feature_currencies.business.CurrencyResult
import com.tmartins.feature_currencies.business.GetAllCurrenciesUseCase
import com.tmartins.feature_currencies.business.GetCurrenciesResult
import com.tmartins.feature_currencies.view.CurrencyUiModel
import com.tmartins.feature_currencies.view.all_currencies.AllCurrenciesViewModel.Event
import com.tmartins.feature_currencies.view.all_currencies.AllCurrenciesViewModel.State
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
internal class AllCurrenciesViewModelTest {

    private val getAllCurrenciesUseCase = mockk<GetAllCurrenciesUseCase>()
    private val strings = mockk<StringResourceWrapper>()
    private val dispatcher = TestCoroutineDispatcher()

    private val viewModel = AllCurrenciesViewModel(
        getAllCurrenciesUseCase = getAllCurrenciesUseCase,
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
    fun `GIVEN GetAllCurrenciesUseCase is Success WHEN the view first launch THEN state is correct`() =
        collector.test { states, _ ->
            coEvery { getAllCurrenciesUseCase() } returns GetCurrenciesResult.Success(
                listOf(
                    CurrencyResult("GBP", "Pound sterling"),
                    CurrencyResult("EUR", "Euro"),
                    CurrencyResult("USD", "United States dollar"),
                )
            )

            viewModel.onFirstLaunch()

            val expectedStates = listOf(
                State(false, emptyList()),
                State(true, emptyList()),
                State(
                    false,
                    listOf(
                        CurrencyUiModel("GBP", "Pound sterling"),
                        CurrencyUiModel("EUR", "Euro"),
                        CurrencyUiModel("USD", "United States dollar"),
                    )
                )
            )
            assertEquals(expectedStates, states)
        }

    @Test
    fun `GIVEN GetAllCurrenciesUseCase is NoInternet WHEN the view first launch THEN state is correct`() =
        collector.test { states, events ->
            coEvery { getAllCurrenciesUseCase() } returns GetCurrenciesResult.NoInternet

            viewModel.onFirstLaunch()

            val expectedStates = listOf(
                State(false, emptyList()),
                State(true, emptyList()),
                State(false, emptyList())
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
    fun `GIVEN GetAllCurrenciesUseCase is ServerError WHEN the view first launch THEN state is correct`() =
        collector.test { states, events ->
            coEvery { getAllCurrenciesUseCase() } returns GetCurrenciesResult.ServerError

            viewModel.onFirstLaunch()

            val expectedStates = listOf(
                State(false, emptyList()),
                State(true, emptyList()),
                State(false, emptyList())
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
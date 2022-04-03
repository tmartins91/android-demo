package com.tmartins.feature_currencies.business

import com.tmartins.feature_currencies.data.CurrenciesRepository
import com.tmartins.feature_currencies.data.CurrencyBaseRatesResponse
import com.tmartins.feature_currencies.data.CurrencyBaseResponse
import com.tmartins.feature_currencies.data.GetCurrenciesBaseResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class GetAllCurrenciesByBaseUseCaseDefaultTest {

    private val repository: CurrenciesRepository = mockk()
    private val getAllCurrenciesByBaseUseCase = GetAllCurrenciesByBaseUseCaseDefault(repository)

    @Test
    fun `GIVEN the repository returns NoInternet WHEN getAllCurrenciesByBaseUseCase THEN NoInternet should be returned`() =
        runBlocking {
            //GIVEN
            val base = "GBP"
            coEvery {
                repository.getAllCurrenciesByBase(base)
            } returns GetCurrenciesBaseResponse.NoInternet

            //WHEN
            val actual = getAllCurrenciesByBaseUseCase(base)

            //THEN
            assertEquals(GetCurrenciesBaseResult.NoInternet, actual)
        }

    @Test
    fun `GIVEN the repository returns ServerError WHEN getAllCurrenciesByBaseUseCase THEN Server Error should be returned`() =
        runBlocking {
            //GIVEN
            val base = "GBP"
            coEvery {
                repository.getAllCurrenciesByBase(base)
            } returns GetCurrenciesBaseResponse.ServerError

            //WHEN
            val actual = getAllCurrenciesByBaseUseCase(base)

            //THEN
            assertEquals(GetCurrenciesBaseResult.ServerError, actual)
        }

    @Test
    fun `GIVEN the repository returns Success AND stripeApiType is intent WHEN getAllCurrenciesByBaseUseCase THEN Success should be returned`() =
        runBlocking {
            //GIVEN
            val base = "GBP"
            coEvery {
                repository.getAllCurrenciesByBase(base)
            } returns GetCurrenciesBaseResponse.Success(
                CurrencyBaseResponse(
                    date = "2021-04-01",
                    currencies = listOf(
                        CurrencyBaseRatesResponse("EUR", 2.0),
                        CurrencyBaseRatesResponse("USD", 3.0)
                    )
                )
            )

            //WHEN
            val actual = getAllCurrenciesByBaseUseCase(base)

            //THEN
            assertEquals(
                GetCurrenciesBaseResult.Success(
                    CurrencyBaseResult(
                        date = "2021-04-01",
                        currencies = listOf(
                            CurrencyBaseRatesResult("EUR", 2.0),
                            CurrencyBaseRatesResult("USD", 3.0)
                        )
                    )
                ),
                actual
            )
        }
}
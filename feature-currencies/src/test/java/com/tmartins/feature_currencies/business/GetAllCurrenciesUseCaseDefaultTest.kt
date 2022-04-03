package com.tmartins.feature_currencies.business

import com.tmartins.feature_currencies.data.CurrenciesRepository
import com.tmartins.feature_currencies.data.CurrencyResponse
import com.tmartins.feature_currencies.data.GetCurrenciesResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

internal class GetAllCurrenciesUseCaseDefaultTest {
    
    private val repository: CurrenciesRepository = mockk()
    private val getAllCurrenciesUseCase = GetAllCurrenciesUseCaseDefault(repository)

    @Test
    fun `GIVEN the repository returns NoInternet WHEN getAllCurrenciesUseCase THEN NoInternet should be returned`() =
        runBlocking {
            //GIVEN
            coEvery {
                repository.getAllCurrencies()
            } returns GetCurrenciesResponse.NoInternet

            //WHEN
            val actual = getAllCurrenciesUseCase()

            //THEN
            assertEquals(GetCurrenciesResult.NoInternet, actual)
        }

    @Test
    fun `GIVEN the repository returns ServerError WHEN getAllCurrenciesUseCase THEN Server Error should be returned`() =
        runBlocking {
            //GIVEN
            coEvery {
                repository.getAllCurrencies()
            } returns GetCurrenciesResponse.ServerError

            //WHEN
            val actual = getAllCurrenciesUseCase()

            //THEN
            assertEquals(GetCurrenciesResult.ServerError, actual)
        }

    @Test
    fun `GIVEN the repository returns Success AND stripeApiType is intent WHEN getAllCurrenciesUseCase THEN Success should be returned`() =
        runBlocking {
            //GIVEN
            coEvery {
                repository.getAllCurrencies()
            } returns GetCurrenciesResponse.Success(
                listOf(
                    CurrencyResponse("GBP", "Pound sterling"),
                    CurrencyResponse("EUR", "Euro"),
                    CurrencyResponse("USD", "United States dollar"),
                )
            )

            //WHEN
            val actual = getAllCurrenciesUseCase()

            //THEN
            assertEquals(
                GetCurrenciesResult.Success(
                    listOf(
                        CurrencyResult("GBP", "Pound sterling"),
                        CurrencyResult("EUR", "Euro"),
                        CurrencyResult("USD", "United States dollar"),
                    )
                ),
                actual
            )
        }
}
package com.tmartins.feature_currencies.data

import com.google.gson.JsonSyntaxException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException

internal class CurrenciesRepositoryDefaultTest {

    private val api = mockk<CurrenciesApi>()
    private val repository = CurrenciesRepositoryDefault(api)

    @Test
    fun `GIVEN api throws JsonSyntaxException WHEN getAllCurrencies THEN ServerError should be returned`() =
        runBlocking {
            coEvery { api.getAllCurrencies() } throws JsonSyntaxException("JSON error")

            val actual = repository.getAllCurrencies()

            assertEquals(GetCurrenciesResponse.ServerError, actual)
        }

    @Test
    fun `GIVEN there is no internet WHEN getAllCurrencies THEN NoInternet should be returned`() =
        runBlocking {
            coEvery { api.getAllCurrencies() } throws UnknownHostException("Unable to resolve host")

            val actual = repository.getAllCurrencies()

            assertEquals(GetCurrenciesResponse.NoInternet, actual)
        }

    @Test
    fun `GIVEN the backend returns ServerError WHEN getAllCurrencies THEN ServerError`() =
        runBlocking {
            coEvery { api.getAllCurrencies() } returns Response.error(
                500,
                "Internal Server Error".toResponseBody()
            )

            val actual = repository.getAllCurrencies()

            assertEquals(GetCurrenciesResponse.ServerError, actual)
        }

    @Test
    fun `GIVEN the backend returns success WHEN getAllCurrencies THEN success should be returned`() =
        runBlocking {
            val jsonResponse = mapOf(
                "GBP" to "Pound sterling",
                "EUR" to "Euro",
                "USD" to "United States dollar"
            )
            coEvery { api.getAllCurrencies() } returns Response.success(jsonResponse)

            val actual = repository.getAllCurrencies()

            val expectedResponse = listOf(
                CurrencyResponse("GBP", "Pound sterling"),
                CurrencyResponse("EUR", "Euro"),
                CurrencyResponse("USD", "United States dollar"),
            )
            val expectedGetResponse = GetCurrenciesResponse.Success(expectedResponse)
            assertEquals(expectedGetResponse, actual)
        }

    @Test
    fun `GIVEN api throws JsonSyntaxException WHEN getAllCurrenciesByBase THEN ServerError should be returned`() =
        runBlocking {
            val base = "GBP"
            coEvery { api.getAllCurrenciesByBase(base) } throws JsonSyntaxException("JSON error")

            val actual = repository.getAllCurrenciesByBase(base)

            assertEquals(GetCurrenciesBaseResponse.ServerError, actual)
        }

    @Test
    fun `GIVEN there is no internet WHEN getAllCurrenciesByBase THEN NoInternet should be returned`() =
        runBlocking {
            val base = "GBP"
            coEvery { api.getAllCurrenciesByBase(base) } throws UnknownHostException("Unable to resolve host")

            val actual = repository.getAllCurrenciesByBase(base)

            assertEquals(GetCurrenciesBaseResponse.NoInternet, actual)
        }

    @Test
    fun `GIVEN the backend returns ServerError WHEN getAllCurrenciesByBase THEN ServerError`() =
        runBlocking {
            val base = "GBP"
            coEvery { api.getAllCurrenciesByBase(base) } returns Response.error(
                500,
                "Internal Server Error".toResponseBody()
            )

            val actual = repository.getAllCurrenciesByBase(base)

            assertEquals(GetCurrenciesBaseResponse.ServerError, actual)
        }

    @Test
    fun `GIVEN the backend returns success WHEN getAllCurrenciesByBase THEN success should be returned`() =
        runBlocking {
            val base = "GBP"
            val jsonResponse = mutableMapOf(
                "date" to "2021-04-01",
                "gbp" to mapOf(
                    "EUR" to 2.0,
                    "USD" to 3.0
                )
            )
            coEvery { api.getAllCurrenciesByBase(base) } returns Response.success(jsonResponse)

            val actual = repository.getAllCurrenciesByBase(base)

            val expectedResponse = CurrencyBaseResponse(
                date = "2021-04-01",
                currencies = listOf(
                    CurrencyBaseRatesResponse("EUR", 2.0),
                    CurrencyBaseRatesResponse("USD", 3.0)
                )
            )
            val expectedGetResponse = GetCurrenciesBaseResponse.Success(expectedResponse)
            assertEquals(expectedGetResponse, actual)
        }
}
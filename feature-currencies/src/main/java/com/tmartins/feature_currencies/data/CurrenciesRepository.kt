package com.tmartins.feature_currencies.data

import com.google.gson.JsonSyntaxException
import java.io.IOException
import javax.inject.Inject

internal interface CurrenciesRepository {
    suspend fun getAllCurrencies(): GetCurrenciesResponse
    suspend fun getAllCurrenciesByBase(baseCurrency: String): GetCurrenciesBaseResponse
}

internal class CurrenciesRepositoryDefault @Inject constructor(
    private val api: CurrenciesApi
)  : CurrenciesRepository {

    override suspend fun getAllCurrencies(): GetCurrenciesResponse {
        return try {
            val response = api.getAllCurrencies()
            return if (response.isSuccessful) {
                GetCurrenciesResponse.Success(response.body()!!.allCurrenciesToResponse())
            } else {
                GetCurrenciesResponse.ServerError
            }
        } catch (e: IOException) {
            GetCurrenciesResponse.NoInternet
        } catch (e: JsonSyntaxException) {
            GetCurrenciesResponse.ServerError
        }
    }

    override suspend fun getAllCurrenciesByBase(baseCurrency: String): GetCurrenciesBaseResponse {
        return try {
            val response = api.getAllCurrenciesByBase(baseCurrency)
            return if (response.isSuccessful) {
                GetCurrenciesBaseResponse.Success(response.body()!!.currencyRatesToResponse())
            } else {
                GetCurrenciesBaseResponse.ServerError
            }
        } catch (e: IOException) {
            GetCurrenciesBaseResponse.NoInternet
        } catch (e: JsonSyntaxException) {
            GetCurrenciesBaseResponse.ServerError
        }
    }
}
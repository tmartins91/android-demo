package com.tmartins.feature_currencies.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

internal interface CurrenciesApi {

    @GET("/gh/fawazahmed0/currency-api@1/latest/currencies.json")
    suspend fun getAllCurrencies(): Response<Map<String, String>>

    @GET("/gh/fawazahmed0/currency-api@1/latest/currencies/{currencyCode}.json")
    suspend fun getAllCurrenciesByBase(
        @Path("currencyCode") currencyCode: String
    ): Response<Map<String, Any>>
}

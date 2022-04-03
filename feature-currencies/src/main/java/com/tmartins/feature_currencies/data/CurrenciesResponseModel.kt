package com.tmartins.feature_currencies.data

internal sealed class GetCurrenciesResponse {
    data class Success(val currencies: List<CurrencyResponse>) : GetCurrenciesResponse()
    object NoInternet : GetCurrenciesResponse()
    object ServerError : GetCurrenciesResponse()
}

internal sealed class GetCurrenciesBaseResponse {
    data class Success(val currencyBase: CurrencyBaseResponse) : GetCurrenciesBaseResponse()
    object NoInternet : GetCurrenciesBaseResponse()
    object ServerError : GetCurrenciesBaseResponse()
}

internal data class CurrencyResponse(
    val code: String,
    val name: String
)

internal data class CurrencyBaseResponse(
    val date: String,
    val currencies: List<CurrencyBaseRatesResponse>
)

internal data class CurrencyBaseRatesResponse(
    val code: String,
    val value: Double
)
package com.tmartins.feature_currencies.business

internal sealed class GetCurrenciesResult {
    data class Success(val currencies: List<CurrencyResult>) : GetCurrenciesResult()
    object NoInternet : GetCurrenciesResult()
    object ServerError : GetCurrenciesResult()
}

internal sealed class GetCurrenciesBaseResult {
    data class Success(val currencyBase: CurrencyBaseResult) : GetCurrenciesBaseResult()
    object NoInternet : GetCurrenciesBaseResult()
    object ServerError : GetCurrenciesBaseResult()
}

internal data class CurrencyResult(
    val code: String,
    val name: String
)

internal data class CurrencyBaseResult(
    val date: String,
    val currencies: List<CurrencyBaseRatesResult>
)

internal data class CurrencyBaseRatesResult(
    val code: String,
    val value: Double
)
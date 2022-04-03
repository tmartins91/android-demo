package com.tmartins.feature_currencies.business

import com.tmartins.feature_currencies.data.CurrencyBaseRatesResponse
import com.tmartins.feature_currencies.data.CurrencyBaseResponse
import com.tmartins.feature_currencies.data.CurrencyResponse

internal fun CurrencyResponse.toResult() = CurrencyResult(
    code = code,
    name = name
)

internal fun CurrencyBaseRatesResponse.toResult() = CurrencyBaseRatesResult(
    code = code,
    value = value
)

internal fun CurrencyBaseResponse.toResult() = CurrencyBaseResult(
    date = date,
    currencies = currencies.map { it.toResult() }
)
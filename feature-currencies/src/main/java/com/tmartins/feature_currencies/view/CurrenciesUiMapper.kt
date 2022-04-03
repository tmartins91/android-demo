package com.tmartins.feature_currencies.view

import com.tmartins.feature_currencies.business.CurrencyBaseRatesResult
import com.tmartins.feature_currencies.business.CurrencyResult

internal fun CurrencyResult.toUi() = CurrencyUiModel(
    code = code,
    name = name
)

internal fun CurrencyBaseRatesResult.toUi() = CurrencyValueUiModel(
    code = code,
    value = value
)
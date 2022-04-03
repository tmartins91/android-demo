package com.tmartins.feature_currencies.view

internal data class CurrencyUiModel(
    val code: String,
    val name: String
)

internal data class CurrencyValueUiModel(
    val code: String,
    val value: Double
)
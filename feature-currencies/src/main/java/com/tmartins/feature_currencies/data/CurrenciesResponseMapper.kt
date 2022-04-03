package com.tmartins.feature_currencies.data

internal fun Map<String, String>.allCurrenciesToResponse() = map { CurrencyResponse(it.key, it.value) }.toList()

internal fun Map<String, Any>.currencyRatesToResponse(): CurrencyBaseResponse {
    var date = ""
    val values = mutableListOf<CurrencyBaseRatesResponse>()
    map { body ->
        when (body.value) {
            is String -> date = body.value as String
            is Map<*, *> -> (body.value as Map<*, *>).map {
                if (it.key is String && it.value is Double) {
                    values.add(
                        CurrencyBaseRatesResponse(
                            code = it.key as String,
                            value = it.value as Double
                        )
                    )
                }
            }
            else -> Unit
        }
    }

    return CurrencyBaseResponse(date, values)
}
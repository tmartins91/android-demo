package com.tmartins.feature_currencies.navigation

import android.app.Activity
import android.content.Intent
import com.tmartins.feature_currencies.view.currency_rates.CurrencyRatesActivity

internal interface CurrenciesNavigator {
    fun start(activity: Activity, baseCurrency: String)
}

internal class CurrenciesNavigatorDefault : CurrenciesNavigator {

    companion object {
        const val BASE_CURRENCY: String = "BASE_CURRENCY"
    }

    override fun start(activity: Activity, baseCurrency: String) {
        val intent = Intent(activity, CurrencyRatesActivity::class.java).apply {
            putExtra(BASE_CURRENCY, baseCurrency)
        }
        activity.startActivity(intent)
    }

}
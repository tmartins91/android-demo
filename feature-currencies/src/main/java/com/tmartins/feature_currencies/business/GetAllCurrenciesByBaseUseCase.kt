package com.tmartins.feature_currencies.business

import com.tmartins.feature_currencies.data.CurrenciesRepository
import com.tmartins.feature_currencies.data.GetCurrenciesBaseResponse
import javax.inject.Inject

internal interface GetAllCurrenciesByBaseUseCase {
    suspend operator fun invoke(baseCurrency: String): GetCurrenciesBaseResult
}

internal class GetAllCurrenciesByBaseUseCaseDefault @Inject constructor(
    private val repository: CurrenciesRepository
) : GetAllCurrenciesByBaseUseCase {

    override suspend fun invoke(baseCurrency: String): GetCurrenciesBaseResult {
        return when (val response = repository.getAllCurrenciesByBase(baseCurrency)) {
            is GetCurrenciesBaseResponse.Success -> GetCurrenciesBaseResult.Success(response.currencyBase.toResult())
            GetCurrenciesBaseResponse.NoInternet -> GetCurrenciesBaseResult.NoInternet
            GetCurrenciesBaseResponse.ServerError -> GetCurrenciesBaseResult.ServerError
        }
    }
}
package com.tmartins.feature_currencies.business

import com.tmartins.feature_currencies.data.CurrenciesRepository
import com.tmartins.feature_currencies.data.GetCurrenciesResponse
import javax.inject.Inject

internal interface GetAllCurrenciesUseCase {
    suspend operator fun invoke(): GetCurrenciesResult
}

internal class GetAllCurrenciesUseCaseDefault @Inject constructor(
    private val repository: CurrenciesRepository
) : GetAllCurrenciesUseCase {

    override suspend fun invoke(): GetCurrenciesResult {
        return when (val response = repository.getAllCurrencies()) {
            is GetCurrenciesResponse.Success -> GetCurrenciesResult.Success(response.currencies.map { it.toResult() })
            GetCurrenciesResponse.NoInternet -> GetCurrenciesResult.NoInternet
            GetCurrenciesResponse.ServerError -> GetCurrenciesResult.ServerError
        }
    }
}
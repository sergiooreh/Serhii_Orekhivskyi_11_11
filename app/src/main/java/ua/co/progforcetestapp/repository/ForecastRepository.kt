package ua.co.progforcetestapp.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ua.co.progforcetestapp.api.ForecastApi
import ua.co.progforcetestapp.model.ForecastEntity
import ua.co.progforcetestapp.model.api.Forecast
import ua.co.progforcetestapp.utility.Resource
import javax.inject.Inject

class ForecastRepository @Inject constructor(
        private val forecastApi: ForecastApi
) {
    fun getForecast(lat: Double, lon: Double): Flow<Resource<ForecastEntity>> = flow {
        emit(Resource.loading(null))
        val response = try {
            forecastApi.getForecast(lat,lon)
        } catch (e: Exception){
            null
        }
        if (response != null && response.isSuccessful){
            emit(Resource.success(ForecastEntity(
                    lat = lat,
                    lon = lon,
                    city = response.body()?.city?.name ?: "",
                    list = response.body()?.list ?: emptyList()
            )))
        }
    }

}
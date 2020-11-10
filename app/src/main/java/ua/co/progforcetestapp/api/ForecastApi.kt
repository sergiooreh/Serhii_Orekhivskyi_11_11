package ua.co.progforcetestapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.co.progforcetestapp.model.Forecast
import ua.co.progforcetestapp.utility.Constants.API_KEY

interface ForecastApi {

    @GET("data/2.5/weather")
    suspend fun getForecast(
        @Query(value = "lat")
        latitude: Int = 1,
        @Query(value = "lon")
        lon: Int = 1,
        @Query(value = "appid")
        apiKey: String = API_KEY
    ): Response<Forecast>
}
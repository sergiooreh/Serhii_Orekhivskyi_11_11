package ua.co.progforcetestapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ua.co.progforcetestapp.model.api.Forecast
import ua.co.progforcetestapp.utility.Constants.API_KEY

interface ForecastApi {

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query(value = "lat")
        latitude: Double = 1.0,
        @Query(value = "lon")
        lon: Double = 1.0,
        @Query(value = "appid")
        apiKey: String = API_KEY
    ): Response<Forecast>
}
package ua.co.progforcetestapp.model.api

data class Forecast(
        val city: City,
        val cnt: Int,
        val cod: String,
        val list: List<ForecastTimeStamp>,
        val message: Int
)
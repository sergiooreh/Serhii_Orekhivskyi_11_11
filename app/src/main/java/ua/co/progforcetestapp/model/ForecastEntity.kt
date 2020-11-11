package ua.co.progforcetestapp.model

import ua.co.progforcetestapp.model.api.ForecastTimeStamp

data class ForecastEntity(
        val lat: Double,
        val lon: Double,
        val city: String,
        val list: List<ForecastTimeStamp>
)
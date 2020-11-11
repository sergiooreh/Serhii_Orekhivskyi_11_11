package ua.co.progforcetestapp.model.api

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)
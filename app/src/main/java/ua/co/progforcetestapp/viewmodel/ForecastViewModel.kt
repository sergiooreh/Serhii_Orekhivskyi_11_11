package ua.co.progforcetestapp.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ua.co.progforcetestapp.model.ForecastEntity
import ua.co.progforcetestapp.model.api.Forecast
import ua.co.progforcetestapp.repository.ForecastRepository
import ua.co.progforcetestapp.utility.Resource

class ForecastViewModel @ViewModelInject constructor(
    private val forecastRepository: ForecastRepository
): ViewModel() {

    private var _forecast: MutableLiveData<Resource<ForecastEntity>> = MutableLiveData()
    val forecast: LiveData<Resource<ForecastEntity>> = _forecast

    fun getForecast(lat: Double, lon: Double) {
        forecastRepository.getForecast(lat, lon)
                .onEach { _forecast.value = it }
                .launchIn(viewModelScope)
    }
}
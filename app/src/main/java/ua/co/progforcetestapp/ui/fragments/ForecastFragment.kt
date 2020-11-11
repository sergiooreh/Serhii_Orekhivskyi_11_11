package ua.co.progforcetestapp.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_forecast.*
import ua.co.progforcetestapp.R
import ua.co.progforcetestapp.adapters.BaseAdapter
import ua.co.progforcetestapp.adapters.ForecastForDayAdapter
import ua.co.progforcetestapp.adapters.ForecastForWeekAdapter
import ua.co.progforcetestapp.model.ForecastEntity
import ua.co.progforcetestapp.model.api.ForecastTimeStamp
import ua.co.progforcetestapp.utility.Status
import ua.co.progforcetestapp.utility.toCelsius
import ua.co.progforcetestapp.viewmodel.ForecastViewModel
import java.util.*

@AndroidEntryPoint
class ForecastFragment : Fragment(R.layout.fragment_forecast){

    private lateinit var forecastForDayAdapter: ForecastForDayAdapter
    private lateinit var forecastForWeekAdapter: ForecastForWeekAdapter
    private val forecastViewModel: ForecastViewModel by viewModels()

    private var forecastForWeek: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forecastForDayAdapter = ForecastForDayAdapter()
        forecastForWeekAdapter = ForecastForWeekAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val latitude = arguments?.getFloat("lat")!!
        val longitude = arguments?.getFloat("lon")!!

        setupRecycleView(forecastForDayAdapter)

        forecastViewModel.getForecast(latitude.toDouble(),longitude.toDouble())
        subscribeToObservers()

        forecastType_btn.setOnClickListener {
            forecastForWeek = !forecastForWeek
            forecastType_btn.text = if (forecastForWeek) "Hours" else "Days"
            subscribeToObservers()
        }
    }

    private fun subscribeToObservers(){
        forecastViewModel.forecast.observe(viewLifecycleOwner, { forecast ->
            when (forecast.status) {
                Status.SUCCESS -> {
                    displayProgressBar()

                    if (forecastForWeek){
                        setupRecycleView(forecastForWeekAdapter)

                        val forecastForWeek = forecast.data?.list?.filter {
                            it.dt_txt.substring(11,16) == "12:00"
                        } as MutableList<ForecastTimeStamp>
                        forecastForWeekAdapter.items = forecastForWeek
                    } else{
                        setupRecycleView(forecastForDayAdapter)

                        forecastForDayAdapter.items = forecast.data?.list?.subList(0,5) as MutableList<ForecastTimeStamp>
                    }

                    currentTemp_tv.text = forecast.data.list.subList(0,5).first {
                        it.dt_txt >= GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY).toString()
                    }.main.temp.toCelsius().toString()

                    city_tv.text = forecast?.data.city

                }
                Status.ERROR -> {
                    displayProgressBar()
                    Toast.makeText(requireActivity(), forecast.message, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    displayProgressBar(true)
                }
            }
        })
    }

    private fun displayProgressBar(isDisplayed: Boolean = false){
        progress_bar.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

    private fun setupRecycleView(_adapter: BaseAdapter<ForecastTimeStamp>){
        forecast_rv.apply {
            adapter = _adapter
            layoutManager = LinearLayoutManager(requireContext(),HORIZONTAL,false)
        }
    }
}

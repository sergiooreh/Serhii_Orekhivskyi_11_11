package ua.co.progforcetestapp.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import kotlinx.android.synthetic.main.item_forecast.view.*
import ua.co.progforcetestapp.R
import ua.co.progforcetestapp.model.ForecastEntity
import ua.co.progforcetestapp.model.api.ForecastTimeStamp
import ua.co.progforcetestapp.utility.toCelsius

class ForecastForDayAdapter: BaseAdapter<ForecastTimeStamp>(R.layout.item_forecast) {

    override val differ = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val forecast = items[position]
        holder.itemView.apply {
            time_tv.text = forecast.dt_txt.substring(11,16)
            val temp = forecast.main.temp.toCelsius()
            temp_tv.text = temp.toString()

            val image = when(forecast.weather.first().main){
                "Rain" -> R.drawable.rainy
                "Clouds" -> R.drawable.cloudy
                "Clear" -> R.drawable.sun
                else -> R.drawable.cloudy
            }

            imageView.setImageResource(image)
        }
    }
}

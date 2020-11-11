package ua.co.progforcetestapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import ua.co.progforcetestapp.api.ForecastApi
import ua.co.progforcetestapp.repository.ForecastRepository
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun provideForecastRepository(forecastApi: ForecastApi) = ForecastRepository(forecastApi)
}
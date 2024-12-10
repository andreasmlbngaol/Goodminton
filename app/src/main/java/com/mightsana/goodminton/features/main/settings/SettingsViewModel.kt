package com.mightsana.goodminton.features.main.settings

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.jakewharton.processphoenix.ProcessPhoenix
import com.mightsana.goodminton.MyViewModel
import com.mightsana.goodminton.model.repository.AppRepository
import com.mightsana.goodminton.model.service.AccountService
import com.mightsana.goodminton.model.values.SharedPreference.PREF_DYNAMIC_COLOR
import com.mightsana.goodminton.model.values.SharedPreference.PREF_NAME
import com.mightsana.goodminton.model.values.SharedPreference.PREF_WEATHER_THEME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    accountService: AccountService,
    appRepository: AppRepository,
    application: Application
): MyViewModel(accountService, appRepository, application) {
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
    private val currentDynamicColorEnabled = sharedPreferences.getBoolean(PREF_DYNAMIC_COLOR, false)
    private val currentWeatherThemeEnabled = sharedPreferences.getBoolean(PREF_WEATHER_THEME, false)

    private val _dynamicColorEnabled = MutableStateFlow(currentDynamicColorEnabled)
    val dynamicColorEnabled = _dynamicColorEnabled.asStateFlow()

    private val _weatherThemeEnabled = MutableStateFlow(currentWeatherThemeEnabled)
    val weatherThemeEnabled = _weatherThemeEnabled.asStateFlow()

    fun setWeatherThemeEnabled(enabled: Boolean) {
        _weatherThemeEnabled.value = enabled
        sharedPreferences.edit().putBoolean(PREF_WEATHER_THEME, enabled).apply()
        setShowSnackbar(_weatherThemeEnabled.value != currentWeatherThemeEnabled)
    }

    fun setDynamicColorEnabled(enabled: Boolean) {
        _dynamicColorEnabled.value = enabled
        sharedPreferences.edit().putBoolean(PREF_DYNAMIC_COLOR, enabled).apply()
        setShowSnackbar(_dynamicColorEnabled.value != currentDynamicColorEnabled)
    }

    private val _showSnackbar = MutableStateFlow(false)
    val showSnackbar = _showSnackbar.asStateFlow()

    fun setShowSnackbar(value: Boolean) {
        _showSnackbar.value = value
    }

    fun restartApp() {
        ProcessPhoenix.triggerRebirth(application)
    }

}
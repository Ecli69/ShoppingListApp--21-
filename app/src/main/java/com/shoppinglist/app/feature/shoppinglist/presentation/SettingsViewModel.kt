package com.shoppinglist.app.feature.shoppinglist.presentation

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shoppinglist.app.MainActivity
import com.shoppinglist.app.core.datastore.SettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : ViewModel() {

    val theme = settingsDataStore.themeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "system"
    )

    val language = settingsDataStore.languageFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "en"
    )

    val currency = settingsDataStore.currencyFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = "RUB"
    )

    fun updateTheme(theme: String) {
        viewModelScope.launch {
            settingsDataStore.saveTheme(theme)
        }
    }

    fun updateLanguage(language: String, context: android.content.Context) {
        viewModelScope.launch {
            settingsDataStore.saveLanguage(language)
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
            if (context is android.app.Activity) {
                context.finishAffinity()
            }
        }
    }

    fun updateCurrency(currency: String) {
        viewModelScope.launch {
            settingsDataStore.saveCurrency(currency)
        }
    }
}
package com.shoppinglist.app

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.LocaleListCompat
import androidx.core.os.ConfigurationCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.shoppinglist.app.core.datastore.SettingsDataStore
import com.shoppinglist.app.feature.shoppinglist.presentation.ShoppingListScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Применяем язык перед загрузкой UI
        val savedLanguage = runBlocking {
            settingsDataStore.languageFlow.first()
        }
        setAppLocale(savedLanguage)

        setContent {
            val theme by settingsDataStore.themeFlow.collectAsState(initial = "system")
            val isDarkTheme = when (theme) {
                "dark" -> true
                "light" -> false
                else -> isSystemInDarkTheme()
            }

            ShoppingListAppTheme(
                darkTheme = isDarkTheme
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShoppingListScreen()
                }
            }
        }
    }

    private fun setAppLocale(languageCode: String) {
        val locale = if (languageCode == "ru") {
            Locale("ru", "RU")
        } else {
            Locale("en", "US")
        }

        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        Locale.setDefault(locale)
    }
}

@Composable
fun ShoppingListAppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF4FC3F7),
            onPrimary = Color(0xFF000000),
            primaryContainer = Color(0xFF0288D1),
            onPrimaryContainer = Color(0xFFFFFFFF),
            secondary = Color(0xFF81D4FA),
            onSecondary = Color(0xFF000000),
            secondaryContainer = Color(0xFF0277BD),
            onSecondaryContainer = Color(0xFFFFFFFF),
            tertiary = Color(0xFF4FC3F7),
            onTertiary = Color(0xFF000000),
            background = Color(0xFF121212),
            onBackground = Color(0xFFFFFFFF),
            surface = Color(0xFF1E1E1E),
            onSurface = Color(0xFFFFFFFF),
            surfaceVariant = Color(0xFF2C2C2C),
            onSurfaceVariant = Color(0xFFB0B0B0),
            error = Color(0xFFCF6679),
            onError = Color(0xFF000000)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF1976D2),
            onPrimary = Color(0xFFFFFFFF),
            primaryContainer = Color(0xFFBBDEFB),
            onPrimaryContainer = Color(0xFF0D47A1),
            secondary = Color(0xFF42A5F5),
            onSecondary = Color(0xFFFFFFFF),
            secondaryContainer = Color(0xFFE3F2FD),
            onSecondaryContainer = Color(0xFF1565C0),
            tertiary = Color(0xFF64B5F6),
            onTertiary = Color(0xFFFFFFFF),
            background = Color(0xFFF5F5F5),
            onBackground = Color(0xFF000000),
            surface = Color(0xFFFFFFFF),
            onSurface = Color(0xFF000000),
            surfaceVariant = Color(0xFFEEEEEE),
            onSurfaceVariant = Color(0xFF424242),
            error = Color(0xFFD32F2F),
            onError = Color(0xFFFFFFFF)
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
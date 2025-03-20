package com.axionlabs.keyhive

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.axionlabs.keyhive.navigation.KeyHiveNavigation
import com.axionlabs.keyhive.ui.theme.KeyHiveTheme
import dagger.hilt.android.AndroidEntryPoint

val LocalActivity = compositionLocalOf<FragmentActivity> {
    error("Error")
}

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            KeyHiveTheme(darkTheme = false) {
                CompositionLocalProvider(LocalActivity provides this) {
                    KeyHiveApp()
                }
            }
        }
    }
}

@Composable
fun KeyHiveApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        KeyHiveNavigation()
    }
}
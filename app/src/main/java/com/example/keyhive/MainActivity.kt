package com.example.keyhive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import com.example.keyhive.components.BiometricAuthComponent
import com.example.keyhive.navigation.KeyHiveNavigation
import com.example.keyhive.screens.HomeScreen
import com.example.keyhive.ui.theme.KeyHiveTheme
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
fun KeyHiveApp () {
    Surface(modifier = Modifier.fillMaxSize()) {
        KeyHiveNavigation()
    }
}
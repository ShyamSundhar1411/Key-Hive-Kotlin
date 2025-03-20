package com.axionlabs.keyhive

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.axionlabs.keyhive.navigation.KeyHiveNavigation
import com.axionlabs.keyhive.screens.PermissionPromptScreen
import com.axionlabs.keyhive.ui.theme.KeyHiveTheme
import dagger.hilt.android.AndroidEntryPoint

val LocalActivity = compositionLocalOf<FragmentActivity> {
    error("Error")
}



@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val permissionGranted = mutableStateOf(false)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                permissionGranted.value = true
                Log.d("MainActivity", "Permission Granted ")
            } else {
                Log.d("MainActivity", "Permission Denied ")
                permissionGranted.value = false

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requestStoragePermission()
        } else {
            permissionGranted.value = true
        }

        enableEdgeToEdge()

        setContent {
            KeyHiveTheme(darkTheme = false) {
                CompositionLocalProvider(LocalActivity provides this) {
                    if (permissionGranted.value) {
                        KeyHiveApp()
                    } else {
                        PermissionPromptScreen { openAppSettings() }
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            permissionGranted.value = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        } else {
            permissionGranted.value = true
        }

        Log.d("MainActivity", "Rechecked Permission: ${permissionGranted.value}")
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            permissionGranted.value = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
    private fun openAppSettings() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", packageName, null)
        )
        startActivity(intent)
    }

}


@Composable
fun KeyHiveApp() {
    Surface(modifier = Modifier.fillMaxSize()) {
        KeyHiveNavigation()
    }
}
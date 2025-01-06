package com.example.keyhive.components


import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.buildAnnotatedString
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.navigation.NavController
import com.example.keyhive.LocalActivity
import com.example.keyhive.routes.Routes
import com.example.keyhive.utils.checkExistence
import com.example.keyhive.utils.getVersion


@Composable
fun BiometricAuthComponent(onSuccess: () -> Unit = {}, onError: () -> Unit = {},title: String = "KeyHive Authenticator",subtitle: String = "Unlock to use KeyHive") {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val isBiometricAuthenticated = remember {
        mutableStateOf(false)
    }
    val executor = ContextCompat.getMainExecutor(context)
    val launcherIntent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            run {
                when (result.resultCode) {
                    1 -> {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        isBiometricAuthenticated.value = true
                    }



                    else -> {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                        isBiometricAuthenticated.value = false
                        onError.invoke()
                    }

                }
            }
        }
    )
    val biometricPrompt = BiometricPrompt(
        activity,
        executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                super.onAuthenticationError(errorCode, errString)
                Log.d("Error",errorCode.toString()+errString.toString())
                onError.invoke()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSuccess.invoke()
            }
        }
    )
    val biometricManager = BiometricManager.from(context)
    val biometricTrigger = {biometricManager.checkExistence(onSuccess = {

        val biometricPromptInfo =
            BiometricPrompt.PromptInfo.Builder().setTitle(title)
                .setSubtitle(subtitle)
                .setAllowedAuthenticators(it or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()

        biometricPrompt.authenticate(
            biometricPromptInfo
        )
    }, openSettings = {
        getVersion(aboveVersion9 = {
            val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                putExtra(
                    Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                    BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_STRONG
                )
            }
            launcherIntent.launch(intent)


        }, belowVersion10 = {
            val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
            activity.startActivity(intent)

        })
    },
        onError = {
            Log.d("On Error", "Triggered")
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    )}
    LaunchedEffect(key1 = lifecycleOwner.lifecycle.currentState) {
        if (lifecycleOwner.lifecycle.currentState.isAtLeast(androidx.lifecycle.Lifecycle.State.RESUMED)) {
            biometricTrigger.invoke()
        }
    }
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = object : DefaultLifecycleObserver {

            override fun onPause(owner: LifecycleOwner){
                isBiometricAuthenticated.value = false
            }

            override fun onResume(owner: LifecycleOwner){
                if(!isBiometricAuthenticated.value){
                    biometricTrigger.invoke()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
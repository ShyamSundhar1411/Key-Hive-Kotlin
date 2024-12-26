package com.example.keyhive.components


import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.keyhive.LocalActivity
import com.example.keyhive.routes.Routes
import com.example.keyhive.utils.checkExistence
import com.example.keyhive.utils.getVersion


@Composable
fun BiometricAuthComponent(navController: NavController) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val resultCode = remember {
        mutableIntStateOf(Int.MIN_VALUE)
    }
    val executor = ContextCompat.getMainExecutor(context)
    val launcherIntent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            run {
                when (result.resultCode) {
                    1 -> {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                        resultCode.intValue = 1
                    }

                    2 -> {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    )

    val biometricManager = BiometricManager.from(context)
    LaunchedEffect(key1 = resultCode.intValue) {
        biometricManager.checkExistence(onSuccess = {
            val biometricPromptInfo =
                BiometricPrompt.PromptInfo.Builder().setTitle("KeyHive Authenticator")
                    .setSubtitle("Authenticate")
                    .setAllowedAuthenticators(it or BiometricManager.Authenticators.DEVICE_CREDENTIAL).build()
            val biometricPrompt = BiometricPrompt(
                activity,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(
                        errorCode: Int,
                        errString: CharSequence
                    ) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        navController.navigate(Routes.HomeScreen.name){
                            popUpTo(Routes.BiometricScreen.name){
                                inclusive = true
                            }
                        }
                    }
                }
            )
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
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        )
    }

}
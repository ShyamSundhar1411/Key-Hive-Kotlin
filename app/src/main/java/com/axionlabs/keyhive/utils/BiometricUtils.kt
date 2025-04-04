package com.axionlabs.keyhive.utils

import android.os.Build
import androidx.biometric.BiometricManager

inline fun authenticators(
    aboveVersion9: () -> Int,
    belowVersion10: () -> Int,
): Int =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        aboveVersion9.invoke()
    } else {
        belowVersion10.invoke()
    }

inline fun BiometricManager.checkExistence(
    onSuccess: (Int) -> Unit,
    onError: (String) -> Unit,
    openSettings: () -> Unit,
) {
    val authenticators =
        authenticators(aboveVersion9 = {
            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        }, belowVersion10 = {
            BiometricManager.Authenticators.BIOMETRIC_WEAK
        })
    when (canAuthenticate(authenticators)) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            onSuccess.invoke(authenticators)
        }

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            onError.invoke("Biometric features are currently unavailable")
        }

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            onError.invoke("No biometric features available on this device")
        }

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            openSettings.invoke()
        }

        BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
            onError.invoke("Security update required")
        }

        BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
            onError.invoke("Biometric features are not supported on this device")
        }

        BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
            onError.invoke("Biometric status is unknown")
        }
    }
}

inline fun getVersion(
    aboveVersion9: () -> Unit,
    belowVersion10: () -> Unit,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        aboveVersion9.invoke()
    } else {
        belowVersion10.invoke()
    }
}

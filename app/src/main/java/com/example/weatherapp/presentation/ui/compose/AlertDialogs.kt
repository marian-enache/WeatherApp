package com.example.weatherapp.presentation.ui.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.window.DialogProperties

@Composable
fun LocationSettingsDialog(onConfirm: () -> Unit,
                           onDismiss: () -> Unit) {
    StandardAlertDialog(
        onConfirmation = onConfirm,
        onDismissRequest = onDismiss,
        confirmationButtonText = "Settings",
        dismissButtonText = "Close App",
        dialogTitle = "Location disabled",
        dialogText = "We need to access your location. Please enable it from the Settings page!",
        icon = Icons.Default.LocationOn
    )
}

@Composable
fun LocationPermanentlyDeniedDialog(onConfirm: () -> Unit) {
    StandardAlertDialog(
        onConfirmation = onConfirm,
        onDismissRequest = {},
        confirmationButtonText = "Close App",
        dismissButtonText = "",
        dialogTitle = "Location unavailable",
        dialogText = "The location permission was permanently denied, the App will close!",
        icon = Icons.Default.Info
    )
}

@Composable
fun StandardAlertDialog(
    onConfirmation: () -> Unit,
    onDismissRequest: () -> Unit,
    confirmationButtonText: String,
    dismissButtonText: String,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = { Icon(icon, contentDescription = null) },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        onDismissRequest = { onDismissRequest() },
        confirmButton = {
            TextButton(onClick = { onConfirmation() }) {
                Text(confirmationButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text(dismissButtonText)
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    )
}
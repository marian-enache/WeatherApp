package com.example.weatherapp.presentation.ui.compose

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.model.LocationSuggestion

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LocationTextField(
    isShown: Boolean = false,
    locationSuggestions: List<LocationSuggestion> = emptyList(),
    onInputChange: (String) -> Unit = {},
    onSuggestionClicked: (LocationSuggestion) -> Unit = {},
    onCloseButtonClicked: () -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    if (!isShown) return
    val isKeyboardOpen by keyboardAsState()

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(8.dp, if (isKeyboardOpen) 8.dp else 56.dp)
            .imePadding()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var text by remember { mutableStateOf("") }
            AnimatedVisibility(
                visible = locationSuggestions.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(0.dp, 280.dp)
                    .padding(8.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(locationSuggestions) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    text = it.address
                                    onSuggestionClicked(it)
                                }
                        ) {
                            Text(it.address)
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
            Row {
                OutlinedTextField(
                    value = text,
                    singleLine = true,
                    label = { Text("Enter location") },
                    onValueChange = {
                        text = it
                        onInputChange(it)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                )

                IconButton(onClick = onCloseButtonClicked, Modifier.align(CenterVertically)) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }

        }
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}

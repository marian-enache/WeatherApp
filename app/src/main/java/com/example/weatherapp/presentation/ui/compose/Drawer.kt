package com.example.weatherapp.presentation.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Drawer(drawerState: DrawerState,
           content: @Composable () -> Unit) {
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                favoriteLocations = emptyList(),
                selectedItem = "",
                chooseLocation = {},
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    favoriteLocations: List<String>,
    selectedItem: String = "",
    chooseLocation: (String) -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(modifier) {

        if (favoriteLocations.isEmpty()) return@ModalDrawerSheet

        Text(
            text = "Favorite locations",
            fontSize = TextUnit(12f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(top = 40.dp)
        )
        LazyColumn {
            items(favoriteLocations) { location ->
                NavigationDrawerItem(
                    label = { Text(location) },
                    selected = false,
                    onClick = { chooseLocation(""); closeDrawer() },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }
}

@Preview("Drawer contents")
@Composable
fun PreviewAppDrawer() {
        AppDrawer(
            listOf("Bucharest", "Paris", "Madrid"),
            chooseLocation = {},
            closeDrawer = { }
        )
}
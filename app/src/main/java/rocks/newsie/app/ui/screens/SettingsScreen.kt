package rocks.newsie.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import rocks.newsie.app.data.Settings
import rocks.newsie.app.data.SettingsStore
import rocks.newsie.app.ui.theme.AppTheme

fun NavGraphBuilder.settingsScreen(
    navController: NavController,
    settingsStore: SettingsStore,
) {
    composable("settings") {
        val viewModel = rememberSettingsScreenViewModel(navController, settingsStore)
        SettingsScreen(viewModel)
    }
}

fun NavController.navigateToSettings() {
    this.navigate("settings")
}

data class SettingItem(val key: String)

class SettingsScreenViewModel(
    private val navController: NavController,
    private val settingsStore: SettingsStore,
) {
    val settings = settingsStore.settings

    suspend fun setSwitch(value: Boolean) {
        settingsStore.setSwitchOn(value)
    }

    fun onGoBack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberSettingsScreenViewModel(
    navController: NavController,
    settingsStore: SettingsStore,
): SettingsScreenViewModel {
    return remember {
        SettingsScreenViewModel(navController, settingsStore)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsScreenViewModel) {
    val scrollState = rememberScrollState()
    val settings: Settings by viewModel.settings.collectAsState(initial = Settings())
    val coroutineScope = rememberCoroutineScope()

    val menuItems = remember {
        listOf(
            SettingItem(key = "switch"),
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { viewModel.onGoBack() }) {
                        Icon(Icons.Rounded.ArrowBack, "Go back")
                    }
                },
                title = {
                    Text("Settings")
                },
                actions = {}
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(it)
                    .verticalScroll(scrollState)
            ) {
                menuItems.forEach { _ ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                print("A")
                                // TODO: add action
                            }
                            .padding(vertical = 16.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Switch On")
                        Switch(
                            checked = settings.isSwitchOn,
                            onCheckedChange = { value ->
                                coroutineScope.launch {
                                    viewModel.setSwitch(value)
                                }
                            },
                        )
                    }
                    Divider()
                }
            }

        }
    )
}

@Preview(showBackground = false)
@Composable
fun SettingsScreenPreview() {
    val navController = rememberNavController()
    val ctx = LocalContext.current
    val settingsStore = SettingsStore(context = ctx)
    val viewModel = rememberSettingsScreenViewModel(navController, settingsStore)

    AppTheme {
        SettingsScreen(viewModel = viewModel)
    }
}
package rocks.newsie.app.ui.screens

import android.os.Parcelable
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
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
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
import kotlinx.parcelize.Parcelize
import rocks.newsie.app.data.Settings
import rocks.newsie.app.data.SettingsStore
import rocks.newsie.app.data.rememberSettingsStore
import rocks.newsie.app.ui.theme.AppTheme

fun NavGraphBuilder.settingsScreen(
    navController: NavController,
    settingsStore: SettingsStore,
) {
    composable("settings") {
        val viewModel = rememberSettingsViewModel(
            navController,
            settingsStore,
        )
        SettingsScreen(viewModel)
    }
}

fun NavController.navigateToSettings() {
    this.navigate("settings")
}

class SettingsViewModel(
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

@Parcelize
data class SettingsViewModelSaver(
    val dummy: Int,
) : Parcelable

@Composable
fun rememberSettingsViewModel(
    navController: NavController,
    settingsStore: SettingsStore,
): SettingsViewModel {
    return rememberSaveable(
        inputs = arrayOf(navController, settingsStore),
        saver = Saver(
            save = {
                SettingsViewModelSaver(dummy = 0)
            },
            restore = {
                SettingsViewModel(
                    navController = navController,
                    settingsStore = settingsStore,
                )
            }
        ),
    ) {
        SettingsViewModel(
            navController = navController,
            settingsStore = settingsStore,
        )
    }
}

data class SettingItem(val key: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
) {
    val scrollState = rememberScrollState()
    val settings: Settings by viewModel.settings.collectAsState(initial = Settings())
    val coroutine = rememberCoroutineScope()

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
                                coroutine.launch {
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
    val settingsStore = rememberSettingsStore(context = LocalContext.current)

    AppTheme {
        val viewModel = rememberSettingsViewModel(
            navController = navController,
            settingsStore = settingsStore,
        )
        SettingsScreen(viewModel)
    }
}
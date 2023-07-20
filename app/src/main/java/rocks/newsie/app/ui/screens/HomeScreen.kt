package rocks.newsie.app.ui.screens

import android.os.Parcelable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.parcelize.Parcelize
import rocks.newsie.app.R
import rocks.newsie.app.ui.theme.AppTheme
import rocks.newsie.app.ui.theme.frenchCannonFontFamily

fun NavGraphBuilder.homeScreen(
    navController: NavController,
) {
    composable("home") {
        val viewModel = rememberHomeScreenViewModel(navController)
        HomeScreen(vm = viewModel)
    }
}

fun NavController.navigateToHome() {
    this.navigate("home")
}

class HomeScreenViewModel(
    private val navController: NavController,
    initValue: String = "abc",
) {
    var value by mutableStateOf(initValue)
        private set

    fun setValueAs(newValue: String) {
        value = newValue
    }

    fun onGoToSettings() {
        navController.navigateToSettings()
    }
}

@Parcelize
data class HomeScreenViewModelHolder(val value: String) : Parcelable

@Composable
fun rememberHomeScreenViewModel(
    navController: NavController,
): HomeScreenViewModel {
    Log.d("Home", "Calling remember")
    val saver = Saver<HomeScreenViewModel, HomeScreenViewModelHolder>(
        save = {
            HomeScreenViewModelHolder(value = it.value)
        },
        restore = {
            HomeScreenViewModel(
                navController = navController,
                initValue = it.value
            )
        }
    )
    return rememberSaveable(saver = saver) {
        Log.d("Home", "computing view_model")
        HomeScreenViewModel(navController)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: HomeScreenViewModel,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_grad),
                            contentDescription = "Logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(Modifier.size(16.dp))
                        Text(
                            "Newsie",
                            fontFamily = frenchCannonFontFamily,
                            fontSize = 20.sp
                        )
                    }

                },
                actions = {
                    IconButton(onClick = { vm.onGoToSettings() }) {
                        Icon(Icons.Rounded.Add, "Add a feed")
                    }
                    IconButton(onClick = { vm.onGoToSettings() }) {
                        Icon(Icons.Rounded.Settings, "Open the menu")
                    }
                }
            )
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                Text("Home")
                Text("value= ${vm.value}")
                Button(onClick = { vm.setValueAs("XXXX") }) {
                    Text("Click me")
                }
            }
        }
    )
}

@Preview(showBackground = false)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    val viewModel = rememberHomeScreenViewModel(navController)

    AppTheme {
        HomeScreen(vm = viewModel)
    }
}
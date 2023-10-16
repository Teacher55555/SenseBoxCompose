package com.example.sensebox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.sensebox.ui.compose.boxlist.BoxListScreen
import com.example.sensebox.ui.compose.boxlist.BoxListViewModel
import com.example.sensebox.ui.compose.boxlist.BoxUiState
import com.example.sensebox.ui.compose.home.HomeScreen
import com.example.sensebox.ui.navigation.BoxAppNavHost
import com.example.sensebox.ui.navigation.MyModalNavigationDrawer
import com.example.sensebox.ui.theme.SenseBoxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SenseBoxTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    MyModalNavigationDrawer (
                        navHostController = navController
                    ) { drawerState ->
                        BoxAppNavHost(
                            navController = navController,
                            drawerState = drawerState,
                        )
                    }
                }
            }
        }
    }
}

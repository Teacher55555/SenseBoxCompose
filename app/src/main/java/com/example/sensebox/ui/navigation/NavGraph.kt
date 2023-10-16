package com.example.sensebox.ui.navigation

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sensebox.ui.compose.boxdetail.BoxDetailScreen
import com.example.sensebox.ui.compose.boxfavList.FavBoxScreen
import com.example.sensebox.ui.compose.boxlist.BoxListScreen
import com.example.sensebox.ui.compose.home.HomeScreen
import com.example.sensebox.ui.navigation.NavigationDestination.*
import kotlinx.coroutines.launch

/**
 * A navigation host that defines all pages that will be navigated.
 * In fact, the host does not change the activity and does not replace fragments,
 * but is responsible for showing the current Compose function.
 * It is also responsible for the backStack and the passing of arguments between Compose functions.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxAppNavHost(
    navController: NavHostController,
    drawerState: DrawerState,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    NavHost(
        navController = navController,
        startDestination = Home.getRoute(),
        modifier = modifier
    ) {
        composable(
            route = Home.getRoute()
        ) {
            HomeScreen(
                onStartClick = { BoxList.navigateThis(navController, it) },
                onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
        composable(
            route = BoxList.getRoute(),
            arguments = BoxList.getArguments(ArgumentsType.INT),
        ) {
            BoxListScreen(
                onBoxClick = { BoxDetail.navigateThis(navController, it) },
                onNavigateBack = navController::popBackStack,
            )
        }
        composable(
            route = BoxDetail.getRoute(),
            arguments = BoxDetail.getArguments(ArgumentsType.STRING),
        ) {
            BoxDetailScreen(
                onNavigateBack = navController::popBackStack
            )
        }
        composable(
            route = FavBox.getRoute()
        ) {
            FavBoxScreen(
                onMenuClick = {
                    scope.launch {
                        drawerState.open()
                    }
                },
                onBoxClick = { BoxDetail.navigateThis(navController, it) },
            )
        }
    }
}
package com.example.sensebox.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sensebox.R
import kotlinx.coroutines.launch

/**
 * Navigation panel currently consisting of two pages.
 * Wraps the content that the composable function is and passes the state of the panel (DrawerState)
 * so that the current page can control the opening/closing of the panel.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyModalNavigationDrawer(
    navHostController: NavHostController,
    menuItems: List<Pair<ImageVector, NavigationDestination>> = listOf(
        Pair(Icons.Default.Home, NavigationDestination.Home),
        Pair(Icons.Default.Favorite, NavigationDestination.FavBox)
    ) ,
    content: @Composable (DrawerState) -> Unit,
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf(menuItems.first().second) }
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Image(
                        painter = painterResource(id = R.drawable.senseboxlogo),
                        contentDescription = stringResource(id = R.string.logo),
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(30.dp)
                    )
                    menuItems.forEach {
                        NavigationDrawerItem(
                            icon = { Icon(imageVector = it.first, contentDescription = it.first.name) },
                            label = { Text(text = it.second.name) },
                            selected = selectedItem == it.second,
                            onClick = {
                                scope.launch { drawerState.close() }
                                selectedItem = it.second
                                navHostController.popBackStack()
                                it.second.navigateThis(navHostController,null)
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                    }
                }
            },
            content = {
                content(drawerState)
            }
        )
    }
package com.example.sensebox.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * An interface that defines the navigation pages as well as the routes through them.
 * Static variables are also saved here, storing a reference to arguments if navigation is carried out with passing parameters.
 */

interface NavigationDestination {
    enum class ArgumentsType { INT, STRING }
    val savedStataHandel: String
    val name: String

    fun getRoute() = when (savedStataHandel.isEmpty()) {
        true -> name
        false -> "$name{$savedStataHandel}"
    }
    fun navigateThis (navController: NavController, argument: Any?) {
        argument?.let {
            navController.navigate("$name$argument")
        } ?: navController.navigate(name)
    }
    fun getArguments(argumentsType: ArgumentsType) : List<NamedNavArgument> {
        return listOf(navArgument(savedStataHandel) {
            type = when (argumentsType) {
                ArgumentsType.STRING -> NavType.StringType
                ArgumentsType.INT -> NavType.IntType
            }
        })
    }

    object Home : NavigationDestination {
        override val savedStataHandel: String = ""
        override val name: String = "Home"
    }

    object BoxList : NavigationDestination {
        override val savedStataHandel: String = "distance"
        override val name: String = "boxList"
    }

    object FavBox : NavigationDestination {
        override val savedStataHandel: String = ""
        override val name: String = "Favorites"
    }

    object BoxDetail : NavigationDestination {
        override val savedStataHandel: String = "boxId"
        override val name: String = "boxDetail"
    }
}




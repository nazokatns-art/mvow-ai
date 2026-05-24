package uz.mentorai.focus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uz.mentorai.focus.data.language.Language
import uz.mentorai.focus.i18n.UiStrings
import uz.mentorai.focus.ui.calendar.CalendarScreen
import uz.mentorai.focus.ui.home.HomeScreen
import uz.mentorai.focus.ui.settings.SettingsScreen
import uz.mentorai.focus.ui.theme.MentorColors

private enum class MainTab(val route: String, val icon: ImageVector) {
    HOME("home", Icons.Default.LocalFireDepartment),
    CALENDAR("calendar", Icons.Default.CalendarMonth),
    SETTINGS("settings", Icons.Default.Settings);

    fun label(language: Language): String = when (this) {
        HOME -> UiStrings.tabSession(language)
        CALENDAR -> UiStrings.tabPlan(language)
        SETTINGS -> UiStrings.tabSettings(language)
    }
}

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val language = LocalLanguage.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MentorColors.SurfaceVoid,
        bottomBar = {
            NavigationBar(
                containerColor = MentorColors.SurfaceSteel,
                contentColor = MentorColors.TextPrimary
            ) {
                MainTab.entries.forEach { tab ->
                    val selected = backStackEntry?.destination?.hierarchy
                        ?.any { it.route == tab.route } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label(language)) },
                        label = {
                            Text(
                                text = tab.label(language),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MentorColors.AccentIron,
                            selectedTextColor = MentorColors.AccentIron,
                            unselectedIconColor = MentorColors.TextMuted,
                            unselectedTextColor = MentorColors.TextMuted,
                            indicatorColor = MentorColors.SurfaceIron
                        )
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = MainTab.HOME.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MentorColors.SurfaceVoid)
        ) {
            composable(MainTab.HOME.route) { HomeScreen() }
            composable(MainTab.CALENDAR.route) { CalendarScreen() }
            composable(MainTab.SETTINGS.route) { SettingsScreen() }
        }
    }
}

package com.ad.fd_ml1.navigation.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ad.fd_ml1.gen.presentation.MLScreen
import com.ad.fd_ml1.gen.presentation.MLViewModel
import com.ad.fd_ml1.liveobjectde.presentation.LiveObjectDetectionScreen
import com.ad.fd_ml1.liveobjectde.presentation.LiveObjectDetectionViewModel
import com.ad.fd_ml1.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
  val navController = rememberNavController()
  val drawerState = rememberDrawerState(DrawerValue.Closed)
  val scope = rememberCoroutineScope()

  ModalNavigationDrawer(
    drawerState = drawerState,
    drawerContent = {
      AppDrawerContent(
        navController = navController,
        drawerState = drawerState,
        scope = scope
      )
    }
  ) {
    NavHost(navController = navController, startDestination = Screen.Detection.route) {
      composable(Screen.Detection.route) {
        val vm = hiltViewModel<MLViewModel>()
        MLScreen(
          onDrawerClick = { scope.launch { drawerState.open() } },
          vm = vm,
        )
      }
      composable(Screen.LiveObjectDetection.route) {
        val vm = hiltViewModel<LiveObjectDetectionViewModel>()
        LiveObjectDetectionScreen(
          onDrawerClick = { scope.launch { drawerState.open() } },
          vm = vm
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawerContent(
  navController: NavController,
  drawerState: DrawerState,
  scope: CoroutineScope
) {
  val items = listOf(Screen.Detection, Screen.LiveObjectDetection)
  val navBackStackEntry by navController.currentBackStackEntryAsState()
  val currentRoute = navBackStackEntry?.destination?.route

  ModalDrawerSheet {

    Text("Machine learning for Android", modifier = Modifier.padding(16.dp))
    HorizontalDivider(
      thickness = 8.dp,
      modifier = Modifier.padding(bottom = 16.dp)
    )

    items.forEach { screen ->
      NavigationDrawerItem(
        icon = { Icon(screen.icon, contentDescription = screen.title) },
        label = { Text(screen.title) },
        selected = currentRoute == screen.route,
        onClick = {
          navController.navigate(screen.route) {
            popUpTo(navController.graph.findStartDestination().id) {
              saveState = true
            }
            launchSingleTop = true
            restoreState = true
          }
          scope.launch {
            drawerState.close()
          }
        },
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
      )
    }
  }
}
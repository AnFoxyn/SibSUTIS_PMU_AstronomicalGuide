package com.sibsutis.galaxyapp.presentation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sibsutis.galaxyapp.R
import com.sibsutis.galaxyapp.common.AdvScreen
import com.sibsutis.galaxyapp.common.AstroScreen
import com.sibsutis.galaxyapp.common.CubeScreen
import com.sibsutis.galaxyapp.common.FourScreen
import com.sibsutis.galaxyapp.presentation.AdvScreen.advScreen
import com.sibsutis.galaxyapp.presentation.NewsScreen.NavigationItem
import com.sibsutis.galaxyapp.presentation.NewsScreen.components.FourScreen
import com.sibsutis.galaxyapp.presentation.opengl.AstroScreen
import com.sibsutis.galaxyapp.presentation.opengl.CubeScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    // этому надо будет как-то уехать
    val items = listOf(
        NavigationItem(
            title = R.string.news_word,
            icon = ImageVector.vectorResource(R.drawable.news_icon),
            navigate = { navController.navigate(FourScreen)}
        ),
        NavigationItem(
            title = R.string.astro_word,
            icon = ImageVector.vectorResource(R.drawable.baseline_stars_24),
            navigate = { navController.navigate(AstroScreen)}
        ),
        NavigationItem(
            title = R.string.cube_word,
            icon = ImageVector.vectorResource(R.drawable.baseline_square_24),
            navigate = { navController.navigate(CubeScreen)}
        ),
        NavigationItem(
            title = R.string.adv_word,
            icon = ImageVector.vectorResource(R.drawable.baseline_ad_units_24),
            navigate = { navController.navigate(AdvScreen)}
        )
    )
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Text(
                        text = stringResource(R.string.menu_word),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    items.forEachIndexed { index, navigationItem ->
                        NavigationDrawerItem(
                            label = { Text(text = stringResource(navigationItem.title)) },
                            icon = { Icon(imageVector = navigationItem.icon, contentDescription = "") },
                            selected = index == selectedItemIndex,
                            onClick = {
                                selectedItemIndex = index
                                navigationItem.navigate()
                                scope.launch {
                                    drawerState.close()
                                }
                            }
                        )
                    }
                }
            }, drawerState = drawerState
        ) {
            NavHost(
                navController = navController,
                startDestination = CubeScreen,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<FourScreen> {
                    FourScreen()
                }
                composable<AdvScreen> {
                    advScreen(onExitClick = { navController.navigate(FourScreen) })
                }
                composable<AstroScreen> {
                    AstroScreen(context = LocalContext.current)
                }
                composable<CubeScreen> {
                    CubeScreen(context = LocalContext.current)
                }
            }
        }
    }
}
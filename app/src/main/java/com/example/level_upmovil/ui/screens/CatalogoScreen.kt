package com.example.level_upmovil.ui.screens

import com.example.level_upmovil.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.level_upmovil.navigation.Screen
import com.example.level_upmovil.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(text = "Menú", modifier = Modifier.padding(16.dp))
                NavigationDrawerItem(
                    label = {Text(text = "Inicio")},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Catalogo)
                    }
                )

                NavigationDrawerItem(
                    label = {Text("Perfil")},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        viewModel.navigateTo(Screen.Profile)
                    }
                )
            }
        }

    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {Text(text = "Catálogo")},
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) { Icon(Icons.Default.Menu, contentDescription = "Menú") }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top

            ) {
                Row(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(Color.DarkGray,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .width(180.dp),
                        contentAlignment = Alignment.Center

                        ){
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.play5),
                                contentDescription = "Play Station 5",
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(150.dp)
                                    .padding(top = 15.dp)
                            )

                            Spacer(modifier = Modifier
                                .height(8.dp))

                            Text(text = "Play station 5")

                            Spacer(modifier = Modifier
                                .height(8.dp))

                            Text(text = "$549.990")
                        }

                    }

                    Spacer(modifier = Modifier
                        .width(30.dp))

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .background(Color.DarkGray,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .width(180.dp),
                        contentAlignment = Alignment.Center

                    ){
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.play5),
                                contentDescription = "Play Station 5",
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(150.dp)
                                    .padding(top = 15.dp)
                            )

                            Spacer(modifier = Modifier
                                .height(8.dp))

                            Text(text = "Play station 5")

                            Spacer(modifier = Modifier
                                .height(8.dp))

                            Text(text = "$549.990")
                        }

                    }
                }
                Text(text = "Catálogo")



            }


        }
    }
}
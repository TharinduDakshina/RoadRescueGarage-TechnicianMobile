package com.example.garage.views


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.garage.repository.SetupNavGraph
import com.example.garage.ui.theme.GarageTheme
import com.example.garage.viewModels.GarageSessionViewModel


class MainActivity : ComponentActivity()  {
    private lateinit var authViewModel: GarageSessionViewModel

    lateinit var navController:NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        authViewModel = ViewModelProvider(this).get(GarageSessionViewModel::class.java)
        setContent {

            GarageTheme {
                navController= rememberNavController()
                SetupNavGraph(mainActivity = this, navController = navController as NavHostController)

            }
        }

    }
}





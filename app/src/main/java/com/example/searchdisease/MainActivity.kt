package com.example.searchdisease

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import com.example.searchdisease.ui.MainScreen
import com.example.searchdisease.ui.Main_Screen
import com.example.searchdisease.ui.apiService
import com.example.searchdisease.ui.theme.SearchdiseaseTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchdiseaseTheme {
                // A surface container using the 'background' color from the theme
                Box(Modifier.fillMaxSize()) {
                    MainScreen(apiService = apiService)
                }
            }
        }
    }
}


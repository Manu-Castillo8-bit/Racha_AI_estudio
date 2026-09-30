package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.local.AppDatabase
import com.example.data.local.StudyRepository
import com.example.ui.screens.RachaScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.StreakViewModel
import com.example.ui.viewmodel.StreakViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: StreakViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = StudyRepository(database.studyDao())
        StreakViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RachaScreen(viewModel = viewModel)
            }
        }
    }
}

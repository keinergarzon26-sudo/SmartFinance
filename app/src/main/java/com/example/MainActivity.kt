package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.FinanceAppScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.FinanceViewModel
import com.example.viewmodel.FinanceViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: FinanceViewModel by viewModels {
        FinanceViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FinanceAppScreen(viewModel = viewModel)
                }
            }
        }
    }
}

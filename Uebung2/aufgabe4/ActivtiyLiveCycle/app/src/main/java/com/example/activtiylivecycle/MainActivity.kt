package com.example.activtiylivecycle

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.activtiylivecycle.ui.theme.ActivtiyLiveCycleTheme

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivityLifecycle"

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        showToast("onCreate called")

        enableEdgeToEdge()
        setContent {
            ActivtiyLiveCycleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart called")
        showToast("onStart called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume called")
        showToast("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause called")
        showToast("onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop called")
        showToast("onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
        showToast("onDestroy called")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ActivtiyLiveCycleTheme {
        Greeting("Android")
    }
}

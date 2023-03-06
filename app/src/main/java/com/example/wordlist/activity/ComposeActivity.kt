package com.example.wordlist.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordlist.activity.ui.theme.WordListTheme
import com.example.wordlist.util.MyTools

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyTools.setStatusBar(this)
        setContent {
            WordListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize().padding(10.dp,30.dp,0.dp,10.dp),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("world")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.Black)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WordListTheme {
        Greeting("Android")
    }
}
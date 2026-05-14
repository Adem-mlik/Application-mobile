package com.example.fiestappsem2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fiestappsem2.ui.theme.FiestAppSem2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FiestAppSem2Theme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        StyledText(innerPadding)
    }
}

@Composable
fun StyledText(innerPadding: PaddingValues) {
    Row(
        modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
            .background(Color.DarkGray, shape = RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        // ✅ Image avec resource (mets une image dans drawable)
        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_gallery),
            contentDescription = "Icon",
            modifier = Modifier.size(40.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Hello Ramadon!",
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    FiestAppSem2Theme {
        MainScreen()
    }
}
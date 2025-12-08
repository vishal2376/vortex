package com.vishal2376.vortex

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.vishal2376.vortex.core.presentation.navigation.AppNavigation
import com.vishal2376.vortex.ui.theme.VortexTheme

class MainActivity : ComponentActivity() {
	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			VortexTheme(darkTheme = true, dynamicColor = false) {
				AppNavigation()
			}
		}
	}
}
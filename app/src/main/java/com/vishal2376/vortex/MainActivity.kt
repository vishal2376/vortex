package com.vishal2376.vortex

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vishal2376.vortex.music_visualizer.MusicVisualizerScreen
import com.vishal2376.vortex.ui.theme.VortexTheme

class MainActivity : ComponentActivity() {
	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			VortexTheme {
				Box(
					modifier = Modifier
						.fillMaxSize()
						.background(MaterialTheme.colorScheme.background),
					contentAlignment = Alignment.Center
				) {
//					LearningAGSL()
//					RainyGlassEffectScreen()
					MusicVisualizerScreen()
				}
			}
		}
	}
}
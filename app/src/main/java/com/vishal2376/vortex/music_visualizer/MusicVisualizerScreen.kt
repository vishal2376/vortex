package com.vishal2376.vortex.music_visualizer


import android.graphics.RuntimeShader
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.vishal2376.vortex.R
import com.vishal2376.vortex.core.audio.AudioController
import com.vishal2376.vortex.core.utils.checkAudioPermission
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * Author: Vishal Singh (vishal2376)
 */

@Composable
fun MusicVisualizerScreen() {
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current

	val audioController = remember { AudioController(context) }
	val runtimeShader = remember { RuntimeShader(FRACTAL_SHADER) }
	val shaderBrush = remember { ShaderBrush(runtimeShader) }

	var time by remember { mutableFloatStateOf(0f) }
	var width by remember { mutableFloatStateOf(1f) }
	var height by remember { mutableFloatStateOf(1f) }
	var intensity by remember { mutableFloatStateOf(0.6f) }

	DisposableEffect(lifecycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			if (event == Lifecycle.Event.ON_PAUSE) {
				audioController.release()
			}
		}
		lifecycleOwner.lifecycle.addObserver(observer)
		onDispose {
			lifecycleOwner.lifecycle.removeObserver(observer)
			audioController.release()
		}
	}

	checkAudioPermission(
		onPermissionGranted = {
			audioController.play(R.raw.arise)
		}
	)

	LaunchedEffect(Unit) {
		val startTime = System.nanoTime()
		while (isActive) {
			time = (System.nanoTime() - startTime) / 1_000_000_000f

			val amplitude = audioController.getAmplitude()

			runtimeShader.setFloatUniform("iResolution", width, height)
			runtimeShader.setFloatUniform("iTime", time)
			runtimeShader.setFloatUniform("audioFactor", amplitude * intensity)

			delay(16)
		}
	}

	Box(modifier = Modifier.fillMaxSize()) {
		Canvas(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black)
				.onSizeChanged {
					width = it.width.toFloat()
					height = it.height.toFloat()
				}
		) {
			val trigger = time
			drawRect(brush = shaderBrush)
		}

		Column(
			modifier = Modifier
				.align(Alignment.BottomCenter)
				.background(
					MaterialTheme.colorScheme.background,
					RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
				)
				.padding(32.dp)
		) {
			Text(
				text = "Intensity: ${(intensity * 100).toInt()}%",
				color = Color.White,
				modifier = Modifier.padding(bottom = 8.dp)
			)
			Slider(
				value = intensity,
				onValueChange = { intensity = it },
				valueRange = 0f..3f
			)
		}
	}
}
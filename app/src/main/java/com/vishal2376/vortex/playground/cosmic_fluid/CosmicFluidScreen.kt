package com.vishal2376.vortex.playground.cosmic_fluid

import android.graphics.RuntimeShader
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.dp

@Composable
fun CosmicFluidScreen() {
	CosmicFluidComponent()
}

@Composable
fun CosmicFluidComponent() {
	val shader = remember { RuntimeShader(COSMIC_FLUID_SHADER) }

	var zoom by remember { mutableFloatStateOf(0.3f) }
	var speed by remember { mutableFloatStateOf(1.0f) }
	var hue by remember { mutableFloatStateOf(0.0f) }

	val infiniteTransition = rememberInfiniteTransition(label = "time")
	val time by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 100f,
		animationSpec = infiniteRepeatable(
			animation = tween(durationMillis = 100000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "time",
	)

	Column(modifier = Modifier.fillMaxSize()) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f)
				.pointerInput(Unit) {
					detectTransformGestures { _, _, zoomChange, _ ->
						zoom = (zoom * zoomChange).coerceIn(0.05f, 5f)
					}
				}
				.onSizeChanged { size ->
					shader.setFloatUniform(
						"resolution",
						size.width.toFloat(),
						size.height.toFloat()
					)
				}
				.drawWithCache {
					shader.setFloatUniform("time", time)
					shader.setFloatUniform("zoom", zoom)
					shader.setFloatUniform("speed", speed)
					shader.setFloatUniform("hue", hue)
					onDrawBehind {
						drawRect(brush = ShaderBrush(shader))
					}
				}
		)

		Column(
			modifier = Modifier
				.fillMaxWidth()
				.background(
					MaterialTheme.colorScheme.background,
					RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
				)
				.padding(32.dp)
		) {
			Text("Speed", color = Color.White)
			Slider(
				value = speed,
				onValueChange = { speed = it },
				valueRange = 0.1f..3f
			)

			Text("Zoom", color = Color.White)
			Slider(
				value = zoom,
				onValueChange = { zoom = it },
				valueRange = 0.05f..5f
			)

			Text("Hue", color = Color.White)
			Slider(
				value = hue,
				onValueChange = { hue = it },
				valueRange = 0f..6.28f
			)
		}
	}
}


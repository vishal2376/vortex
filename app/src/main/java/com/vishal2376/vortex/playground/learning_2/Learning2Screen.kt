package com.vishal2376.vortex.playground.learning_2

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import com.vishal2376.vortex.R

@Composable
fun Learning2Screen() {
	val shader = remember { RuntimeShader(EXPERIMENT_SHADER) }

	var touchPosition by remember { mutableStateOf(Offset.Zero) }
	var size by remember { mutableStateOf(IntSize.Zero) }

	val infiniteTransition = rememberInfiniteTransition(label = "experiment")
	val time by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 100f,
		animationSpec = infiniteRepeatable(
			animation = tween(durationMillis = 30000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "time"
	)

	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.onSizeChanged { newSize ->
				size = newSize
				shader.setFloatUniform(
					"resolution",
					newSize.width.toFloat(),
					newSize.height.toFloat()
				)
				if (touchPosition == Offset.Zero) {
					touchPosition = Offset(newSize.width / 2f, newSize.height / 2f)
				}
			}
			.pointerInput(Unit) {
				detectDragGestures(
					onDragStart = { touchPosition = it },
					onDrag = { change, _ -> touchPosition = change.position; change.consume() }
				)
			}
			.graphicsLayer {
				shader.setFloatUniform("time", time)
				shader.setFloatUniform("touchPos", touchPosition.x, touchPosition.y)
				renderEffect = RenderEffect
					.createRuntimeShaderEffect(shader, "contents")
					.asComposeRenderEffect()
			}
	) {
		Image(
			painter = painterResource(id = R.drawable.cyberpunk),
			contentDescription = null,
			contentScale = ContentScale.Crop,
			modifier = Modifier.fillMaxSize()
		)
	}
}
package com.vishal2376.vortex.playground.mask_reveal

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vishal2376.vortex.R

@Composable
fun MaskRevealScreen() {
	var radius by remember { mutableFloatStateOf(120f) }
	var edgeSoftness by remember { mutableFloatStateOf(30f) }
	var invertColors by remember { mutableStateOf(false) }

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
	) {
		MaskRevealEffect(
			radius = radius,
			edgeSoftness = edgeSoftness,
			invertColors = invertColors,
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
		) {
			Image(
				painter = painterResource(id = R.drawable.cyberpunk),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier.fillMaxSize()
			)
		}


		Column(
			modifier = Modifier
				.fillMaxWidth()
				.background(
					MaterialTheme.colorScheme.background,
					RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
				)
				.padding(20.dp)
		) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically
			) {
				Text(
					text = "Invert Colors",
					fontSize = 14.sp,
					color = MaterialTheme.colorScheme.onBackground
				)
				Switch(
					checked = invertColors,
					onCheckedChange = { invertColors = it },
					colors = SwitchDefaults.colors(
						checkedThumbColor = MaterialTheme.colorScheme.primary,
						checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
					)
				)
			}

			Spacer(modifier = Modifier.height(12.dp))

			Text(
				text = "Radius ${radius.toInt()}",
				fontSize = 13.sp,
				color = MaterialTheme.colorScheme.onBackground
			)
			Slider(
				value = radius,
				onValueChange = { radius = it },
				valueRange = 50f..300f,
				colors = SliderDefaults.colors(
					thumbColor = MaterialTheme.colorScheme.primary,
					activeTrackColor = MaterialTheme.colorScheme.primary
				)
			)

			Text(
				text = "Edge Softness ${edgeSoftness.toInt()}",
				fontSize = 13.sp,
				color = MaterialTheme.colorScheme.onBackground
			)
			Slider(
				value = edgeSoftness,
				onValueChange = { edgeSoftness = it },
				valueRange = 5f..100f,
				colors = SliderDefaults.colors(
					thumbColor = MaterialTheme.colorScheme.primary,
					activeTrackColor = MaterialTheme.colorScheme.primary
				)
			)
		}
	}
}

@Composable
fun MaskRevealEffect(
	radius: Float,
	edgeSoftness: Float,
	invertColors: Boolean,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit,
) {
	val shader = remember { RuntimeShader(AMOEBA_MASK_SHADER) }

	var touchPosition by remember { mutableStateOf(Offset.Zero) }
	var isTouching by remember { mutableStateOf(false) }
	var size by remember { mutableStateOf(IntSize.Zero) }

	val animatedRadius by animateFloatAsState(
		targetValue = if (isTouching) radius else radius * 0.8f,
		animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
		label = "radius"
	)

	val infiniteTransition = rememberInfiniteTransition(label = "mask")
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
		modifier = modifier
			.onSizeChanged { newSize ->
				size = newSize
				shader.setFloatUniform(
					"resolution",
					newSize.width.toFloat(),
					newSize.height.toFloat()
				)
				if (touchPosition == Offset.Zero) {
					touchPosition = Offset(
						newSize.width / 2f,
						newSize.height / 2f
					)
				}
			}
			.pointerInput(Unit) {
				detectDragGestures(
					onDragStart = { offset ->
						touchPosition = offset
						isTouching = true
					},
					onDrag = { change, _ ->
						touchPosition = change.position
						change.consume()
					},
					onDragEnd = { isTouching = false },
					onDragCancel = { isTouching = false }
				)
			}
			.graphicsLayer {
				shader.setFloatUniform("time", time)
				shader.setFloatUniform("touchPos", touchPosition.x, touchPosition.y)
				shader.setFloatUniform("radius", animatedRadius)
				shader.setFloatUniform("edgeSoftness", edgeSoftness)
				shader.setFloatUniform("invertMask", if (invertColors) 1f else 0f)

				renderEffect = RenderEffect
					.createRuntimeShaderEffect(shader, "contents")
					.asComposeRenderEffect()
			}
	) {
		content()
	}
}

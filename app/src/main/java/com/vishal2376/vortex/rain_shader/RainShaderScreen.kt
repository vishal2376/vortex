package com.vishal2376.vortex.rain_shader


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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vishal2376.vortex.R

/**
 * Author: Vishal Singh (vishal2376)
 */


@Composable
fun RainyGlassEffectScreen() {
	var rainIntensity by remember { mutableFloatStateOf(0.2f) }

	Box(modifier = Modifier.fillMaxSize()) {

		RainyGlassEffect(
			rainAmount = rainIntensity,
			modifier = Modifier.fillMaxSize()
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
				.align(Alignment.BottomCenter)
				.background(
					MaterialTheme.colorScheme.background,
					RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp)
				)
				.padding(32.dp)
		) {
			Text("Reverse Rain Intensity", color = Color.White)
			Slider(
				value = rainIntensity,
				onValueChange = { rainIntensity = it },
				valueRange = 0f..1f
			)
		}
	}
}


@Composable
private fun RainyGlassEffect(
	rainAmount: Float,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit,
) {
	val shader = remember { RuntimeShader(RAIN_SHADER) }
	val infiniteTransition = rememberInfiniteTransition(label = "rain")
	val time by infiniteTransition.animateFloat(
		initialValue = 0f,
		targetValue = 100f,
		animationSpec = infiniteRepeatable(
			animation = tween(20000, easing = LinearEasing),
			repeatMode = RepeatMode.Restart
		),
		label = "time"
	)

	Box(
		modifier = modifier
			.onSizeChanged { size ->
				shader.setFloatUniform("resolution", size.width.toFloat(), size.height.toFloat())
			}
			.graphicsLayer {
				shader.setFloatUniform("time", time)
				shader.setFloatUniform("rainAmount", rainAmount)

				renderEffect = RenderEffect
					.createRuntimeShaderEffect(shader, "rainWindow")
					.asComposeRenderEffect()
			}
	) {
		content()
	}
}
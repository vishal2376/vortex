package com.vishal2376.vortex.playground.warp_shader

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import android.view.MotionEvent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vishal2376.vortex.R

/**
 * Author: Vishal Singh (vishal2376)
 */

@Composable
fun WarpShaderScreen() {
    var warpIntensity by remember { mutableFloatStateOf(0.5f) }
    var isCircularWarp by remember { mutableStateOf(true) }
    var touchPosition by remember { mutableStateOf(Offset.Zero) }

    Box(modifier = Modifier.fillMaxSize()) {

        WarpEffect(
            warpIntensity = warpIntensity,
            isCircular = isCircularWarp,
            touchPosition = touchPosition,
            onInput = { touchPosition = it },
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Circular Warp", color = Color.White)
                Switch(
                    checked = isCircularWarp,
                    onCheckedChange = { isCircularWarp = it }
                )
            }

            Text("Warp Intensity", color = Color.White)
            Slider(
                value = warpIntensity,
                onValueChange = { warpIntensity = it },
                valueRange = 0f..1f
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun WarpEffect(
    warpIntensity: Float,
    isCircular: Boolean,
    touchPosition: Offset,
    onInput: (Offset) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val shader = remember { RuntimeShader(WARP_SHADER) }
    val infiniteTransition = rememberInfiniteTransition(label = "warp")
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
            .pointerInteropFilter {
                if (isCircular) {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            onInput(Offset(it.x, it.y))
                            true
                        }
                        else -> false
                    }
                } else {
                    false
                }
            }
            .onSizeChanged { size ->
                shader.setFloatUniform("resolution", size.width.toFloat(), size.height.toFloat())
            }
            .graphicsLayer {
                shader.setFloatUniform("time", time)
                shader.setFloatUniform("warpIntensity", warpIntensity)
                shader.setFloatUniform("touchPosition", touchPosition.x, touchPosition.y)
                shader.setIntUniform("mode", if (isCircular) 1 else 0)

                renderEffect = RenderEffect
                    .createRuntimeShaderEffect(shader, "warpShader")
                    .asComposeRenderEffect()
            }
    ) {
        content()
    }
}

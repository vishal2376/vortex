package com.vishal2376.vortex.day0

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.vishal2376.vortex.ui.theme.VortexTheme
import org.intellij.lang.annotations.Language

object Constants {
	const val RESOLUTION = "resolution"
	const val COLOR_A = "colorA"
	const val COLOR_B = "colorB"
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LearningAGSL() {

	@Language("AGSL")
	val shaderInfo = """
		uniform float2 ${Constants.RESOLUTION};
		layout(color) uniform half4 ${Constants.COLOR_A};
		layout(color) uniform half4 ${Constants.COLOR_B};
		
		half4 main(in float2 fragCoord) {
			float2 uv = fragCoord / ${Constants.RESOLUTION}.xy;
			float mixValue = distance(uv, vec2(0,1));
			
			return mix(${Constants.COLOR_A}, ${Constants.COLOR_B} , mixValue);
		}
	""".trimIndent()

	Box(
		modifier = Modifier
			.applyShader(
				shaderInfo = shaderInfo,
				colorA = MaterialTheme.colorScheme.background,
				colorB = MaterialTheme.colorScheme.primary
			)
			.size(200.dp)
	)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Modifier.applyShader(
	shaderInfo: String,
	colorA: Color,
	colorB: Color,
): Modifier {
	return drawWithCache {

		val shader = RuntimeShader(shaderInfo)
		val shaderBrush = ShaderBrush(shader)

		//set variable
		shader.setFloatUniform(Constants.RESOLUTION, size.width, size.height)
		onDrawBehind {
			shader.setColorUniform(
				Constants.COLOR_A,
				android.graphics.Color.valueOf(
					colorA.red,
					colorA.green,
					colorA.blue,
					colorA.alpha,
				)
			)
			shader.setColorUniform(
				Constants.COLOR_B,
				android.graphics.Color.valueOf(
					colorB.red,
					colorB.green,
					colorB.blue,
					colorB.alpha,
				)
			)
			drawRect(shaderBrush)
		}
	}
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@PreviewLightDark
@Composable
private fun LearningAGSLPreview() {
	VortexTheme() {
		LearningAGSL()
	}
}
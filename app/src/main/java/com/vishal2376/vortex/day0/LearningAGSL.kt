package com.vishal2376.vortex.day0

import android.graphics.RuntimeShader
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vishal2376.vortex.ui.theme.VortexTheme
import org.intellij.lang.annotations.Language

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun LearningAGSL() {

	@Language("AGSL")
	val shaderInfo = """
		half4 main(in float2 fragCoord) {
			return half4(1,1,0,1);
		}
	""".trimIndent()

	Box(
		modifier = Modifier
			.applyShader(shaderInfo)
			.size(200.dp)
	)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun Modifier.applyShader(
	shaderInfo: String,
): Modifier {

	val shader = RuntimeShader(shaderInfo)
	val shaderBrush = ShaderBrush(shader)

	return drawWithCache {
		onDrawBehind {
			drawRect(shaderBrush)
		}
	}
}

@Preview
@Composable
private fun LearningAGSLPreview() {
	VortexTheme() {
		LearningAGSL()
	}
}
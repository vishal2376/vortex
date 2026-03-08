package com.vishal2376.vortex.playground.learning_2

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vishal2376.vortex.ui.theme.VortexTheme
import org.intellij.lang.annotations.Language

@Language("AGSL")
val EXPERIMENT_SHADER = """
    uniform float2 resolution;
    uniform float time;
    uniform shader contents;
    uniform float2 touchPos;
    
    half4 main(float2 fragCoord) {
//		// normalize cordinates to (0,0 to 1,1)
//        float2 uv = fragCoord / resolution;
//
//		float2 offset = float2(sin(uv.x * 10.0 + time) * 0.02, 0.0);
//
//		half4 color = contents.eval((uv + offset) * resolution);
//        float dist = length(fragCoord - touchPos);
//float wave = sin(dist * 0.05 - time * 5.0) * 0.5 + 0.5;
//float2 dir = normalize(fragCoord - touchPos);
//float2 offset = dir * wave * 10.0;
//half4 color = contents.eval(fragCoord + offset);
//        return color;
		float2 uv = fragCoord / resolution;
//
//		// sin(a * x + b) * c
//		float a = 10;
//		float b = time;
//		float c = 0.01;
//		float2 offset = float2(sin(a * uv.y + b) * c);

		float2 dir = uv - touchPos / resolution;
		float zoom = 0.9;  // < 1 = zoom in
		float2 zoomedUV = touchPos / resolution + dir * zoom;
		half4 color = contents.eval(zoomedUV * resolution);
			
		return color;
    }
"""


@Preview
@Composable
fun Learning2ScreenPreview() {
	VortexTheme {
		Learning2Screen()
	}
}

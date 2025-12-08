package com.vishal2376.vortex.music_visualizer

import org.intellij.lang.annotations.Language

/**
 * Author: Vishal Singh (vishal2376)
 *
 * Shader Inspired by : @kishimisu
 */


@Language("AGSL")
val FRACTAL_SHADER = """
    uniform float2 iResolution;
    uniform float iTime;
    uniform float audioFactor;

    half3 palette(float t) {
        half3 a = half3(0.5, 0.5, 0.5);
        half3 b = half3(0.5, 0.5, 0.5);
        half3 c = half3(1.0, 1.0, 1.0);
        half3 d = half3(0.263, 0.416, 0.557);

        return a + b * cos(6.28318 * (c * t + d));
    }

    float2 rotate(float2 uv, float angle) {
        float s = sin(angle);
        float c = cos(angle);
        return float2(uv.x * c - uv.y * s, uv.x * s + uv.y * c);
    }

    half4 main(float2 fragCoord) {
        float2 uv = (fragCoord * 2.0 - iResolution.xy) / iResolution.y;
        
        uv = rotate(uv, iTime * 0.2);
        
        float2 uv0 = uv;
        half3 finalColor = half3(0.0);
        
        float audioWarp = 1.0 + audioFactor * 0.2; 

        for (float i = 0.0; i < 4.0; i++) {
            uv = fract(uv * 1.5 * audioWarp) - 0.5;

            float d = length(uv) * exp(-length(uv0));

            half3 col = palette(length(uv0) + i * 0.4 + iTime * 0.4);

            d = sin(d * (8.0 + audioFactor * 4.0) + iTime) / 8.0;
            d = abs(d);

            float glow = 0.01 + (audioFactor * 0.005);
            d = pow(glow / d, 1.2);

            finalColor += col * d;
        }
        
        return half4(finalColor, 1.0);
    }
""".trimIndent()
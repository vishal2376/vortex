package com.vishal2376.vortex.playground.warp_shader

import org.intellij.lang.annotations.Language

/**
 * Author: Vishal Singh (vishal2376)
 */

@Language("AGSL")
val WARP_SHADER = """
    uniform shader warpShader;
    uniform float2 resolution;
    uniform float time;
    uniform float warpIntensity;
    uniform float2 touchPosition;
    uniform int mode;

    half4 main(float2 fragCoord) {
        if (mode == 0) {
            float2 uv = fragCoord.xy / resolution.xy;
            float2 warpedUv = uv;
            warpedUv.x += sin(uv.y * 10.0 + time) * warpIntensity * 0.1;
            warpedUv.y += cos(uv.x * 10.0 + time) * warpIntensity * 0.1;
            return warpShader.eval(warpedUv * resolution.xy);
        } else {
             float dist = distance(fragCoord.xy, touchPosition);
             float radius = min(resolution.x, resolution.y) * 0.3; 
             
             float2 p = fragCoord.xy - touchPosition;
             
             if (dist < radius) {
                 float percent = (radius - dist) / radius;
                 float theta = percent * percent * warpIntensity * 10.0;
                 float s = sin(theta);
                 float c = cos(theta);
                 
                 p = float2(dot(p, float2(c, -s)), dot(p, float2(s, c)));
             }
             
             return warpShader.eval(touchPosition + p);
        }
    }
"""

package com.vishal2376.vortex.playground.cosmic_fluid

import org.intellij.lang.annotations.Language

@Language("AGSL")
val COSMIC_FLUID_SHADER = """
    uniform float2 resolution;
    uniform float time;
    uniform float zoom;
    uniform float speed;
    uniform float hue;

    float3 hueShift(float3 color, float shift) {
        float3 k = float3(0.57735, 0.57735, 0.57735);
        float cosA = cos(shift);
        return color * cosA + cross(k, color) * sin(shift) + k * dot(k, color) * (1.0 - cosA);
    }

    half4 main(float2 fragCoord) {
        float2 r = resolution;
        float t = time * speed;
        
        float2 p = ((fragCoord.xy * 2.0 - r) / r.y / 0.3) / zoom;
        float2 v = float2(0.0, 0.0);
        float4 o = float4(0.0, 0.0, 0.0, 0.0);
        
        for(float i = 0.0; i < 10.0; i += 1.0) {
            v = p;
            for(float f = 0.0; f < 9.0; f += 1.0) {
                v += sin(float2(v.y, v.x) * (f + 1.0) + (i + 1.0) + t) / (f + 1.0);
            }
            o += (cos((i + 1.0) + float4(0.0, 1.0, 2.0, 3.0)) + 1.0) / 6.0 / length(v);
        }
        
        float4 e2 = exp(-2.0 * o * o);
        o = (1.0 - e2) / (1.0 + e2);
        
        float3 shifted = hueShift(o.rgb, hue);
        return half4(half3(shifted), 1.0);
    }
"""

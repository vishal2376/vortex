package com.vishal2376.vortex.playground.mask_reveal

import org.intellij.lang.annotations.Language

@Language("AGSL")
val AMOEBA_MASK_SHADER = """
    uniform float2 resolution;
    uniform float time;
    uniform shader contents;
    uniform float2 touchPos;
    uniform float radius;
    uniform float edgeSoftness;
    uniform float invertMask;
    
    float hash(float2 p) {
        return fract(sin(dot(p, float2(127.1, 311.7))) * 43758.5453);
    }
    
    float noise(float2 p) {
        float2 i = floor(p);
        float2 f = fract(p);
        f = f * f * f * (f * (f * 6.0 - 15.0) + 10.0);
        
        float a = hash(i);
        float b = hash(i + float2(1.0, 0.0));
        float c = hash(i + float2(0.0, 1.0));
        float d = hash(i + float2(1.0, 1.0));
        
        return mix(mix(a, b, f.x), mix(c, d, f.x), f.y);
    }
    
    float fbm(float2 p) {
        float value = 0.0;
        float amplitude = 0.5;
        float frequency = 1.0;
        
        for (int i = 0; i < 5; i++) {
            value += amplitude * noise(p * frequency);
            amplitude *= 0.5;
            frequency *= 2.0;
        }
        return value;
    }
    
    half4 toGrayscale(half4 color) {
        float gray = dot(color.rgb, half3(0.299, 0.587, 0.114));
        return half4(gray, gray, gray, color.a);
    }
    
    half4 main(float2 fragCoord) {
        half4 colorPixel = contents.eval(fragCoord);
        half4 grayPixel = toGrayscale(colorPixel);
        
        float2 diff = fragCoord - touchPos;
        float angle = atan(diff.y, diff.x);
        float dist = length(diff);
        
        float blobOffset = 0.0;
        float angleNorm = (angle + 3.14159) / 6.28318;
        
        blobOffset += fbm(float2(angleNorm * 4.0 + time * 0.3, time * 0.2)) * 50.0;
        blobOffset += sin(angle * 3.0 + time * 1.5) * 15.0;
        blobOffset += sin(angle * 5.0 - time * 2.0) * 10.0;
        blobOffset += sin(angle * 7.0 + time * 0.8) * 8.0;
        
        float wobble = sin(time * 3.0) * 5.0 + cos(time * 2.3) * 3.0;
        blobOffset += wobble;
        
        float effectiveRadius = radius + blobOffset;
        float mask = smoothstep(effectiveRadius + edgeSoftness, effectiveRadius - edgeSoftness, dist);
        
        if (invertMask > 0.5) {
            mask = 1.0 - mask;
        }
        
        half4 finalColor = mix(colorPixel, grayPixel, mask);
        
        float edgeGlow = smoothstep(effectiveRadius + edgeSoftness * 1.5, effectiveRadius, dist) 
                       - smoothstep(effectiveRadius, effectiveRadius - edgeSoftness * 1.5, dist);
        finalColor.rgb += edgeGlow * 0.1;
        
        return finalColor;
    }
"""

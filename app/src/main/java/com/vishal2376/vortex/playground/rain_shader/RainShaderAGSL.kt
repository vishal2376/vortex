package com.vishal2376.vortex.playground.rain_shader



import org.intellij.lang.annotations.Language

@Language("AGSL")
val RAIN_SHADER = """
    uniform shader rainWindow;
    uniform float2 resolution;
    uniform float time;
    uniform float rainAmount;

// --- Credits to BigWings for Heartfelt Math ---
    float3 N13(float p) {
        float3 p3 = fract(float3(p) * float3(.1031, .11369, .13787));
        p3 += dot(p3, p3.yzx + 19.19);
        return fract(float3((p3.x + p3.y) * p3.z, (p3.x + p3.z) * p3.y, (p3.y + p3.z) * p3.x));
    }

    float4 N14(float t) {
        return fract(sin(t * float4(123., 1024., 1456., 264.)) * float4(6547., 345., 8799., 1564.));
    }

    float N(float t) {
        return fract(sin(t * 12345.564) * 7658.76);
    }

    float Saw(float b, float t) {
        return smoothstep(0., b, t) * smoothstep(1., b, t);
    }

    float2 DropLayer2(float2 uv, float t) {
        float2 UV = uv;
        uv.y += t * 0.75;
        float2 a = float2(6., 1.);
        float2 grid = a * 2.;
        float2 id = floor(uv * grid);
        
        float colShift = N(id.x); 
        uv.y += colShift;
        
        id = floor(uv * grid);
        float3 n = N13(id.x * 35.2 + id.y * 2376.1);
        float2 st = fract(uv * grid) - float2(.5, 0);
        
        float x = n.x - .5;
        float y = UV.y * 20.;
        float wiggle = sin(y + sin(y));
        x += wiggle * (.5 - abs(x)) * (n.z - .5);
        x *= .7;
        float ti = fract(t + n.z);
        y = (Saw(.85, ti) - .5) * .9 + .5;
        float2 p = float2(x, y);
        
        float d = length((st - p) * a.yx);
        float mainDrop = smoothstep(.4, .0, d);
        
        float r = sqrt(smoothstep(1., y, st.y));
        float cd = abs(st.x - x);
        float trail = smoothstep(.23 * r, .15 * r * r, cd);
        float trailFront = smoothstep(-.02, .02, st.y - y);
        trail *= trailFront * r * r;
        
        y = UV.y;
        float trail2 = smoothstep(.2 * r, .0, cd);
        float droplets = max(0., (sin(y * (1. - y) * 120.) - st.y)) * trail2 * trailFront * n.z;
        y = fract(y * 10.) + (st.y - .5);
        float dd = length(st - float2(x, y));
        droplets = smoothstep(.3, 0., dd);
        float m = mainDrop + droplets * r * trailFront;
        
        return float2(m, trail);
    }

    float StaticDrops(float2 uv, float t) {
        uv *= 40.;
        float2 id = floor(uv);
        uv = fract(uv) - .5;
        float3 n = N13(id.x * 107.45 + id.y * 3543.654);
        float2 p = (n.xy - .5) * .7;
        float d = length(uv - p);
        float fade = Saw(.025, fract(t + n.z));
        float c = smoothstep(.3, 0., d) * fract(n.z * 10.) * fade;
        return c;
    }

    float2 Drops(float2 uv, float t, float l0, float l1, float l2) {
        float s = StaticDrops(uv, t) * l0; 
        float2 m1 = DropLayer2(uv, t);
        float2 m2 = DropLayer2(uv * 1.85, t);
        float c = s + m1.x * l1 + m2.x * l2;
        c = smoothstep(.3, 1., c);
        return float2(c, max(m1.y * l1, m2.y * l2));
    }

    float hash(float2 p) { return fract(sin(dot(p, float2(127.1, 311.7))) * 43758.5453); }
    float noise(float2 p) {
        float2 i = floor(p); float2 f = fract(p);
        float a = hash(i); float b = hash(i + float2(1., 0.));
        float c = hash(i + float2(0., 1.)); float d = hash(i + float2(1., 1.));
        float2 u = f * f * (3. - 2. * f);
        return mix(a, b, u.x) + (c - a) * u.y * (1. - u.x) + (d - b) * u.x * u.y;
    }

    half4 main(float2 fragCoord) {
        float2 uv = fragCoord.xy / resolution.y;
        float2 UV = fragCoord.xy / resolution.xy;
        
        float T = time * 0.2;
        
        float staticDrops = smoothstep(-.5, 1., rainAmount) * 2.;
        float layer1 = smoothstep(.25, .75, rainAmount);
        float layer2 = smoothstep(.0, .5, rainAmount);

        float2 c = Drops(uv, T, staticDrops, layer1, layer2);
        
        float2 e = float2(.001, 0.);
        float cx = Drops(uv + e, T, staticDrops, layer1, layer2).x;
        float cy = Drops(uv + e.yx, T, staticDrops, layer1, layer2).x;
        float2 n = float2(cx - c.x, cy - c.x); 

        float frost = noise(uv * 100.0) * 0.005;

        float2 distortion = n + (frost * (1.0 - c.x)); 
        
        half4 col = rainWindow.eval((UV + distortion) * resolution.xy);
        
        col.rgb += smoothstep(0.0, 0.1, c.x) * 0.1;

        return col;
    }
"""
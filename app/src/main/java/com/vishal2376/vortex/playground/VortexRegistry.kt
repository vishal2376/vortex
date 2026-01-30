package com.vishal2376.vortex.playground

import com.vishal2376.vortex.core.presentation.app.VortexDemo
import com.vishal2376.vortex.playground.learning.LearningShadersDemo
import com.vishal2376.vortex.playground.mask_reveal.MaskRevealDemo
import com.vishal2376.vortex.playground.music_visualizer.MusicVisualizerDemo
import com.vishal2376.vortex.playground.rain_shader.RainShaderDemo
import com.vishal2376.vortex.playground.warp_shader.WarpShaderDemo

object VortexRegistry {
	val demos: List<VortexDemo> = listOf(
		LearningShadersDemo,
		RainShaderDemo,
		MusicVisualizerDemo,
		WarpShaderDemo,
		MaskRevealDemo,
	)
}
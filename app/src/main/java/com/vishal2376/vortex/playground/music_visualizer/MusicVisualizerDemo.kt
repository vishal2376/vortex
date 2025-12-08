package com.vishal2376.vortex.playground.music_visualizer

import androidx.compose.runtime.Composable
import com.vishal2376.vortex.core.presentation.app.VortexDemo
import com.vishal2376.vortex.core.presentation.components.BaseVortexScreen

/**
 * Author: Vishal Singh (vishal2376)
 */


object MusicVisualizerDemo : VortexDemo {
	override val title: String
		get() = "Music Visualizer"
	override val route: String
		get() = "music_visualizer"

	@Composable
	override fun BaseScreen(onClickBack: () -> Unit) {
		BaseVortexScreen(
			title = title,
			onClickBack = onClickBack
		) { MusicVisualizerScreen() }
	}
}
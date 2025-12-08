package com.vishal2376.vortex.playground.learning

import androidx.compose.runtime.Composable
import com.vishal2376.vortex.core.presentation.app.VortexDemo
import com.vishal2376.vortex.core.presentation.components.BaseVortexScreen

/**
 * Author: Vishal Singh (vishal2376)
 */


object LearningShadersDemo : VortexDemo {
	override val title: String
		get() = "Learning Shaders"
	override val route: String
		get() = "learning_shaders"

	@Composable
	override fun BaseScreen(onClickBack: () -> Unit) {
		BaseVortexScreen(
			title = title,
			onClickBack = onClickBack
		) { LearningAGSL() }
	}
}
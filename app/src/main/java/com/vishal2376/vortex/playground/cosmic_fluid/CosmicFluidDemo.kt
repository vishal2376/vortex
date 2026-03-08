package com.vishal2376.vortex.playground.cosmic_fluid

import androidx.compose.runtime.Composable
import com.vishal2376.vortex.core.presentation.app.VortexDemo
import com.vishal2376.vortex.core.presentation.components.BaseVortexScreen

object CosmicFluidDemo : VortexDemo {
	override val title: String
		get() = "Cosmic Fluid"
	override val route: String
		get() = "cosmic_fluid"

	@Composable
	override fun BaseScreen(onClickBack: () -> Unit) {
		BaseVortexScreen(
			title = title,
			onClickBack = onClickBack
		) {
			CosmicFluidScreen()
		}
	}
}

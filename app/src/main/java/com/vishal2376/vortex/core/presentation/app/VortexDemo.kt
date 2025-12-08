package com.vishal2376.vortex.core.presentation.app

import androidx.compose.runtime.Composable

interface VortexDemo {
	val title: String
	val route: String

	@Composable
	fun BaseScreen(onClickBack: () -> Unit)
}
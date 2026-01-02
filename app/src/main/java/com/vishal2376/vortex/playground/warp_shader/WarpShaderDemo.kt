package com.vishal2376.vortex.playground.warp_shader

import androidx.compose.runtime.Composable
import com.vishal2376.vortex.core.presentation.app.VortexDemo
import com.vishal2376.vortex.core.presentation.components.BaseVortexScreen

/**
 * Author: Vishal Singh (vishal2376)
 */

object WarpShaderDemo : VortexDemo {
    override val title: String
        get() = "Warp Shader"
    override val route: String
        get() = "warp_shader"

    @Composable
    override fun BaseScreen(onClickBack: () -> Unit) {
        BaseVortexScreen(
            title = title,
            onClickBack = onClickBack
        ) { WarpShaderScreen() }
    }
}

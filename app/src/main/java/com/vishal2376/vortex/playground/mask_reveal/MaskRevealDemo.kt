package com.vishal2376.vortex.playground.mask_reveal

import androidx.compose.runtime.Composable
import com.vishal2376.vortex.core.presentation.app.VortexDemo
import com.vishal2376.vortex.core.presentation.components.BaseVortexScreen

/**
 * Author: Vishal Singh (vishal2376)
 */

object MaskRevealDemo : VortexDemo {
    override val title: String
        get() = "Mask Reveal"
    override val route: String
        get() = "mask_reveal"

    @Composable
    override fun BaseScreen(onClickBack: () -> Unit) {
        BaseVortexScreen(
            title = title,
            onClickBack = onClickBack
        ) { MaskRevealScreen() }
    }
}

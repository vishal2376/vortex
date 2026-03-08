package com.vishal2376.vortex.playground.learning_2

import androidx.compose.runtime.Composable
import com.vishal2376.vortex.core.presentation.app.VortexDemo
import com.vishal2376.vortex.core.presentation.components.BaseVortexScreen

object Learning2Demo : VortexDemo {
    override val title: String
        get() = "Learning 2"
    override val route: String
        get() = "learning_2"

    @Composable
    override fun BaseScreen(onClickBack: () -> Unit) {
        BaseVortexScreen(
            title = title,
            onClickBack = onClickBack
        ) { Learning2Screen() }
    }
}

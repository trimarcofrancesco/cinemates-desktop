package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.viewmodel.SplashViewModel
import javafx.geometry.Pos
import tornadofx.*

class SplashView : View("Cinemates") {
    private val viewModel: SplashViewModel by inject()

    override val root = vbox {
        alignment = Pos.CENTER
    }

    override fun onDock() {
        primaryStage.width = 1200.0
        primaryStage.height = 600.0

        viewModel.loadNextView()
    }

}

package it.unina.cinemates_desktop.view

import tornadofx.*

class HomeView : View("Hello TornadoFX") {

    override val root = hbox {
        label("ciao dalla home")
    }

    override fun onDock() {
        primaryStage.width = 1200.0
        primaryStage.height = 600.0
    }

}

package it.unina.cinemates_desktop

import it.unina.cinemates_desktop.util.AppPreferences
import it.unina.cinemates_desktop.view.SplashView
import javafx.stage.Stage
import tornadofx.App

class MyApp: App(SplashView::class, Styles::class) {
    override fun start(stage: Stage) {
        with(stage) {
            width = 1200.0
            height = 600.0
        }

        AppPreferences.init()

        super.start(stage)
    }
}
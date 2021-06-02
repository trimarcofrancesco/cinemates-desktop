package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.model.UserModel
import it.unina.cinemates_desktop.viewmodel.LoginViewModel
import javafx.geometry.Pos
import javafx.scene.text.FontWeight
import tornadofx.*

class HomeView : View("Cinemates") {
    private val user: UserModel by inject()
    private val loginViewModel: LoginViewModel by inject()

    override val root = vbox {
        alignment = Pos.CENTER

        label(user.profile.value.username) {
            style {
                fontWeight = FontWeight.BOLD
                fontSize = 24.px
            }
        }

        button("Logout").action(loginViewModel::logout)
    }

    override fun onDock() {
        primaryStage.width = 1200.0
        primaryStage.height = 600.0
    }

}

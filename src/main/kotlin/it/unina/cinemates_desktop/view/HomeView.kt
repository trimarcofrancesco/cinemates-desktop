package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.model.UserModel
import it.unina.cinemates_desktop.viewmodel.LoginViewModel
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.layout.BorderPane
import tornadofx.*

class HomeView : View("Cinemates") {
    private val user: UserModel by inject()
    private val loginViewModel: LoginViewModel by inject()

    override val root : BorderPane by fxml("/views/HomeView.fxml")

    val timerangeCombobox : ComboBox<String> by fxid("timerangeCombobox")

    init {
        timerangeCombobox.items = FXCollections.observableArrayList("Sempre", "Settimana", "Mese")
        timerangeCombobox.value = "Sempre"
    }

    override fun onDock() {
        primaryStage.width = 1200.0
        primaryStage.height = 600.0
    }

}

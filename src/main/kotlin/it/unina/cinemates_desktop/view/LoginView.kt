package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.Styles
import it.unina.cinemates_desktop.viewmodel.LoginViewModel
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Alert
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*

class LoginView : View("Cinemates") {
    private val homeView : HomeView by inject()
    private val viewModel : LoginViewModel by inject()

    private val username = viewModel.bind { SimpleStringProperty() }
    private val password = viewModel.bind { SimpleStringProperty() }

    init {
        println("LoginView init")

        subscribe<LoginViewModel.NetworkErrorEvent> {
            println("NetworkErrorEvent")
            alert(Alert.AlertType.WARNING, header = it.message, owner = currentWindow)

            println(it.message)
        }

        subscribe<LoginViewModel.GenericErrorEvent> {
            println("GenericErrorEvent")
            it.genericError?.let { genericError -> alert(Alert.AlertType.WARNING, header = genericError.message, owner = currentWindow) }
            println(it.genericError?.message)
        }

        subscribe<LoginViewModel.LoginSucceededEvent> {
            println("LoginSucceededEvent")
            println(it.loginResponse?.toString())
            this@LoginView.replaceWith(homeView, transition = ViewTransition.FadeThrough(.5.seconds), centerOnScreen = true)
            runLater {
                homeView.shakeHomeView()
            }
        }
    }

    override val root = vbox {
        form {
            fieldset(labelPosition = Orientation.VERTICAL) {
                fieldset("Username") {
                    textfield(username).required()
                }
                fieldset("Password") {
                    passwordfield(password).required()
                }
                button("Accedi") {
                    enableWhen(viewModel.valid)
                    isDefaultButton = true
                    useMaxHeight = true
                    action {
                        GlobalScope.launch {
                            viewModel.login(username.value, password.value)
                        }
                    }
                    addClass(Styles.redButton)
                    style {
                        prefWidth = 200.px
                    }
                }
                style {
                    alignment = Pos.CENTER
                    maxWidth = 500.px
                }
            }
            style {
                alignment = Pos.CENTER
            }
        }
        style {
            alignment = Pos.CENTER
        }
    }

    override fun onDock() {
        username.value = ""
        password.value = ""
        viewModel.clearDecorators()
    }
}

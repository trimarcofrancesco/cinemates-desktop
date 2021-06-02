package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.viewmodel.LoginViewModel
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Orientation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*

class LoginView : View("Cinemates") {
    private val viewModel : LoginViewModel by inject()

    private val username = viewModel.bind { SimpleStringProperty() }
    private val password = viewModel.bind { SimpleStringProperty() }

    init {
        subscribe<LoginViewModel.NetworkErrorEvent> {
            println("NetworkErrorEvent")
            println(it.message)
        }

        subscribe<LoginViewModel.GenericErrorEvent> {
            println("GenericErrorEvent")
            println(it.genericError?.message)
        }

        subscribe<LoginViewModel.LoginSucceededEvent> {
            println("LoginSucceededEvent")
            println(it.loginResponse?.profile.toString())
            this@LoginView.replaceWith(HomeView::class, transition = ViewTransition.FadeThrough(.5.seconds), centerOnScreen = true)
        }
    }

    override val root = vbox {
        form {
            fieldset(labelPosition = Orientation.VERTICAL) {
                fieldset("Username") {
                    textfield(username).required()
                }
                fieldset("Password") {
                    textfield(password).required()
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
                }
            }
        }
    }

    override fun onDock() {
        username.value = ""
        password.value = ""
        viewModel.clearDecorators()
    }
}

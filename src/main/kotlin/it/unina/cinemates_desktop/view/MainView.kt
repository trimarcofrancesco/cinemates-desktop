package it.unina.cinemates_desktop.view

import it.unina.cinemates_desktop.viewmodel.LoginViewModel
import javafx.geometry.Orientation
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tornadofx.*

class MainView : View("Hello TornadoFX") {
    private val viewModel : LoginViewModel by inject()

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
        }
    }

    override val root = form {
        fieldset(labelPosition = Orientation.VERTICAL) {
            fieldset("Username") {
                textfield(viewModel.username).required()
            }
            fieldset("Password") {
                textfield(viewModel.password).required()
            }
            button("Accedi") {
                enableWhen(viewModel.valid)
                isDefaultButton = true
                useMaxHeight = true
                action {
                    GlobalScope.launch {
                        viewModel.login(viewModel.username.value, viewModel.password.value)
                    }
                }
            }
        }
    }

}
